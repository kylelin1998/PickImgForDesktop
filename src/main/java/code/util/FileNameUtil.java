package code.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileNameUtil {

    public static String getCustomFileName(byte[] fileByte, String rule, String originalName) {
        String baseName = FilenameUtils.getBaseName(originalName);
        String extension = FilenameUtils.getExtension(originalName);

        String s = rule;
        s = StringUtils.replace(s, "${baseName}", baseName);
        s = StringUtils.replace(s, "${extension}", extension);
        s = StringUtils.replace(s, "${uuid}", UUID.randomUUID().toString());
        s = StringUtils.replace(s, "${datetime}", new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.S").format(new Date()));
        s = StringUtils.replace(s, "${timestamp}", String.valueOf(System.currentTimeMillis()));
        if (StringUtils.contains(s, "${md5}")) {
            s = StringUtils.replace(s, "${md5}", DigestUtils.md5Hex(fileByte));
        }
        if (StringUtils.contains(s, "${sha1}")) {
            s = StringUtils.replace(s, "${sha1}", DigestUtils.sha1Hex(fileByte));
        }

        if (StringUtils.endsWith(s, ".") && StringUtils.isBlank(extension)) {
            s = StringUtils.removeEnd(s, ".");
        }

        return s;
    }

}
