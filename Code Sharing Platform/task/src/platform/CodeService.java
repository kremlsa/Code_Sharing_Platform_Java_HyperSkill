package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CodeService {

    @Autowired
    private CodeRepository codeRepository;

    public CodeDB findById(final long id) {
        try {
            Optional<CodeDB> codeDB = codeRepository.findById(id);
            System.out.println(codeDB.get().getId());
            return codeDB.get();
        } catch (Exception exc) {
            System.out.println(id);
            return null;
        }
    }

    public CodeDB findByUuid(final String uuid) {
        try {
            Optional<CodeDB> codeDB = codeRepository.findByUuid(uuid);
            System.out.println(codeDB.get().getId());
            return codeDB.get();
        } catch (Exception exc) {
            System.out.println(uuid);
            return null;
        }
    }

    public void update(CodeDB codeDB) {
        //codeRepository.update(codeDB);
    }

    public void updateViews(String uuid, Long views) {
        CodeDB temp = findByUuid(uuid);
        temp.setViews(views);
        codeRepository.save(temp);
    }

    public Long findTopByOrderByIdDesc() {
        try {
            Optional<CodeDB> codeDB = codeRepository.findTopByOrderByIdDesc();
            System.out.println(codeDB.get().getId());
            return codeDB.get().getId();
        } catch (Exception exc) {
            return null;
        }
    }

    public void deleteCodeDBbyUuid(CodeDB codeDB) {
            codeRepository.delete(codeDB);
    }

    public void saveCodeDB(CodeDB codeDB) {
        codeRepository.save(codeDB);
    }



}
