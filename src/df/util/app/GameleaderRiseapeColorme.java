package df.util.app;

import android.content.Context;
import df.util.ad.Colorme;
import df.util.android.ResourceUtil;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-16
 * Time: ����7:49
 * To change this template use File | Settings | File Templates.
 */
public class GameleaderRiseapeColorme {
    // �����ֵܿ����װ汾
    public static final String TAG = "df.util.GameleaderRiseapeColorme";

    // ��ͬ��Ʒ������
    public static final String colorme_riseape_name = "���֣������ֵ�";
    public static final String colorme_riseape_version = "�汾��V2.0";
    public static final String colorme_riseape_catalog = "Ӧ�����ͣ�������";


    // �Ƿ���colorme���ض��˵����������ִ��֮
    public static boolean isColormeMenuRiseape(final Context context, final int viewId) {

        final int aboutId = ResourceUtil.getIdResourceIdFromName(context, Colorme.ID_COLORME_ABOUT_BUTTON);
        if (viewId == aboutId) {
            String product = colorme_riseape_name + Colorme.DELIM_LINE
                    + colorme_riseape_version + Colorme.DELIM_LINE
                    + colorme_riseape_catalog;
            Colorme.clickMenuAbout(context, product);
            return true;
        }

        final int moreId = ResourceUtil.getIdResourceIdFromName(context, Colorme.ID_COLORME_MORE_BUTTON);
        if (viewId == moreId) {
            Colorme.clickMenuMore(context);
            return true;
        }

        final int quitId = ResourceUtil.getIdResourceIdFromName(context, Colorme.ID_COLORME_QUIT_BUTTON);
        if (viewId == quitId) {
            Colorme.clickMenuQuit(context);
            return true;
        }
        return false;
    }

}
