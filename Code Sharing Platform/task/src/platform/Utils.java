package platform;
import freemarker.template.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.io.*;

public class Utils {
    public static String init(CodeDB codeDB, String template) {
        /* ------------------------------------------------------------------------ */
        /* You should do this ONLY ONCE in the whole application life-cycle:        */

        /* Create and adjust the configuration singleton */
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        try {
            cfg.setDirectoryForTemplateLoading(new File("templates"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Recommended settings for new projects:
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);

        /* ------------------------------------------------------------------------ */
        /* You usually do these for MULTIPLE TIMES in the application life-cycle:   */
        String date = codeDB.getDate();
        String code = codeDB.getCode();
        Long views = codeDB.getViews();
        Long time = codeDB.getTime();
        Template temp = null;
        Map root = null;
        Long delta = time - (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - codeDB.getCreationTime());

        if (views > 0 && time > 0) {
            root = new HashMap();
            root.put("views",  (views == 0 ? "0" : String.valueOf(views - 1)));
            root.put("time", (time <= 0 ? "0" : String.valueOf(delta)));
            root.put("title", template);
            root.put("date", date);
            root.put("code", code);
            try {
                temp = cfg.getTemplate("codeSecret.tmpl");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (views > 0) {
            root = new HashMap();
            root.put("views",  (views == 0 ? "0" : String.valueOf(views - 1)));
            root.put("title", template);
            root.put("date", date);
            root.put("code", code);
            try {
                temp = cfg.getTemplate("codeSecretv.tmpl");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (time > 0) {
            root = new HashMap();
            root.put("time", (time <= 0 ? "0" : String.valueOf(delta)));
            root.put("title", template);
            root.put("date", date);
            root.put("code", code);
            try {
                temp = cfg.getTemplate("codeSecrett.tmpl");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            /* Create a data-model */
            root = new HashMap();
            root.put("title", template);
            root.put("date", date);
            root.put("code", code);
            try {
                temp = cfg.getTemplate("code.tmpl");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* Get the template (uses cache internally) */
        String result = "";

        /* Merge data-model with template */
        Writer out = new StringWriter();
        try {
            temp.process(root, out);
            result = out.toString();
            System.out.println("Recieve3");
        } catch (TemplateException e) {
            System.out.println("Recieve2");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Recieve1");
            e.printStackTrace();
        }
        System.out.println("Recieve");
        // Note: Depending on what `out` is, you may need to call `out.close()`.
        // This is usually the case for file output, but not for servlet output.
        return result;
    }

    public static String generateUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}

