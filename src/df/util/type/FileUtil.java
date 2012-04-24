package df.util.type;

import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public static void close(InputStream is) {
        try {
            if (is != null) is.close();
        } catch (IOException e) {
        }
    }

    public static void close(OutputStream os) {
        try {
            if (os != null) os.close();
        } catch (IOException e) {
        }
    }

    public static byte[] readInputStream(InputStream is) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();

        return os.toByteArray();
    }

    public static byte[] readAssetFile(String levelFilePathName, AssetManager am) throws Exception {
        InputStream is = am.open(levelFilePathName);
        return readInputStream(is);
    }
}