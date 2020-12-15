package platform;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;


public class Code {
    public long id = 0;
    public String code;
    public String date;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    JSONObject jo;
    Map<Long, JSONObject> map = new LinkedHashMap<>();
    CodeService cs;


    Code (CodeService cs) {
        this.cs = cs;
        this.code = "public static void main(String[] args) {" +
                "SpringApplication.run(CodeSharingPlatform.class, args);}";
        this.date = getDate(LocalDateTime.now());
        jo = new JSONObject();
        jo.put("code", code);
        jo.put("date", date);
        map.put(id, jo);
    }

    public String getDate(LocalDateTime date) {
        return date.format(formatter);
    }

    public String setCode(String body){
        LocalDateTime ct = LocalDateTime.now();
        this.date = getDate(ct);
        jo = new JSONObject(body);
        jo.put("date", date);
        id++;
        //map.put(id, jo);
        CodeDB codeDB = new CodeDB();
        codeDB.setCreationTime(ct.toEpochSecond(ZoneOffset.UTC));
        codeDB.setCode(jo.getString("code"));
        codeDB.setDate(jo.getString("date"));
        codeDB.setTime(jo.getLong("time"));
        codeDB.setViews(jo.getLong("views"));
        codeDB.setUuid(Utils.generateUuid());
        cs.saveCodeDB(codeDB);
        return "{ \"id\" : \"" + codeDB.getUuid() + "\"}";
    }

    public String getJsonById(String uuid) {
        processSecretTime(cs.findByUuid(uuid));
        CodeDB codeDB = cs.findByUuid(uuid);
        Long delta = codeDB.getTime() - (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - codeDB.getCreationTime());
        JSONObject temp = new JSONObject();
        temp.put("date", codeDB.getDate());
        temp.put("code", codeDB.getCode());
        //temp.put("time", codeDB.getTime());
        temp.put("time", (delta <= 0 ? 0 : delta));
        temp.put("views", (codeDB.getViews() <= 0 ? 0 : codeDB.getViews() - 1));
        processSecretView(codeDB);
        return temp.toString();
    }

    public String getJsonLatest() {
        String res = "[";
        CodeDB codeDB;
        id = cs.findTopByOrderByIdDesc();
        int counter = 10;
        while (true) {
            if (id == 0) break;
            if (counter == 0) break;
            if (cs.findById(id) != null) {
                codeDB = cs.findById(id);
                if (codeDB.getTime() == 0 && codeDB.getViews() == 0) {
                    JSONObject temp = new JSONObject();
                    temp.put("date", codeDB.getDate());
                    temp.put("code", codeDB.getCode());
                    temp.put("time", codeDB.getTime());
                    temp.put("views", codeDB.getViews());
                    res +=  temp.toString() + ",";
                    counter--;
                }
            }
            id--;
        }
        if (res.length() > 2)
        res = res.substring(0, res.length() - 1);
        res += "]";
        return res;
    }

    public String getWebById(String uuid) {
        processSecretTime(cs.findByUuid(uuid));
        String res = " ";
        CodeDB codeDB = cs.findByUuid(uuid);
        System.out.println("Send");
        res = Utils.init(codeDB, "Code");
        processSecretView(codeDB);
        return res;
    }

    public String getWebLatest() {
        String res = "";
        id = cs.findTopByOrderByIdDesc();
        int counter = 10;
        while (true) {
            if (id == 0) break;
            if (counter == 0) break;
            CodeDB codeDB;
            if (cs.findById(id) != null) {
                codeDB = cs.findById(id);
                if (codeDB.getTime() == 0 && codeDB.getViews() == 0) {
                    res += Utils.init(codeDB, "Latest") + "\n";
                    counter--;
                }
            }
            id--;
        }
        return res;
    }

    public String getForm() {
        return "<html><head><title>Create</title></head><body><pre>" +
                "<textarea id=\"code_snippet\"></textarea>" +
                "<br>" +
                "<label for=\"time\">Time restriction:</label>" +
                "<input type=\"text\" id=\"time\" name=\"time\" value=\"0\"><br>" +
                "<label for=\"views\">View restriction:</label>" +
                "<input type=\"text\" id=\"views\" name=\"views\" value=\"0\"><br>" +
                "<button id=\"send_snippet\" type=\"submit\" onclick=\"send()\">Submit</button>" +
                "</pre></body></html>" +
                "<script>" +
                "function send() {\n" +
                "    let object = {\n" +
                "        \"code\": document.getElementById(\"code_snippet\").value,\n" +
                "        \"time\": document.getElementById(\"time\").value,\n" +
                "        \"views\": document.getElementById(\"views\").value\n" +
                "    };\n" +
                "    \n" +
                "    let json = JSON.stringify(object);\n" +
                "    \n" +
                "    let xhr = new XMLHttpRequest();\n" +
                "    xhr.open(\"POST\", '/api/code/new', false)\n" +
                "    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');\n" +
                "    xhr.send(json);\n" +
                "    \n" +
                "    if (xhr.status == 200) {\n" +
                "      alert(\"Success!\");\n" +
                "    }\n" +
                "}" +
                "</script>";
    }

    public void processSecretView(CodeDB codeDB) {
        Long views = codeDB.getViews();
        String uuid = codeDB.getUuid();
        if (views > 0) {
            views--;
            if (views == 0) {
                cs.deleteCodeDBbyUuid(codeDB);
            } else {
                cs.updateViews(uuid, views);
            }
        }
    }

    public void processSecretTime(CodeDB codeDB) {
        Long time = codeDB.getTime();
        String uuid = codeDB.getUuid();
        Long delta = (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - codeDB.getCreationTime());
        System.out.println("delta is " + delta + "**" + codeDB.getTime() + "&&" + codeDB.getViews());
        if (time > 0) {
            if (delta > time) {
                cs.deleteCodeDBbyUuid(codeDB);
            } else {

            }
        }
    }

}
