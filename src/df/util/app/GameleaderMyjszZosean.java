package df.util.app;

import android.content.Context;
import df.util.ad.Colorme;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-16
 * Time: ����7:49
 * To change this template use File | Settings | File Templates.
 */
public class GameleaderMyjszZosean {
    // ���ž�ʯ��������Ѷ�汾
    public static final String TAG = "df.util.GameleaderMyjszZosean";

    // ��ͬ��Ʒ������
    public static final String colorme_myjsz_name = "���֣����ž�ʯ��";
    public static final String colorme_myjsz_version = "�汾��V2.0";
    public static final String colorme_myjsz_catalog = "Ӧ�����ͣ�������";
    // ������Ϣ
    public static final String zosean_about_company =
            "�����̣�����������Ѷ�Ƽ����޹�˾";
    public static final String zosean_about_tele =
            "�ͷ��绰��18618185892";
    public static final String zosean_about_copyright =
            "����������" +
                    "����Ϸ�İ�Ȩ�顰����������Ѷ�Ƽ����޹�˾�����У�" +
                    "��Ϸ�е����֡�ͼƬ�����ݾ�Ϊ��Ϸ��Ȩ�����ߵĸ���" +
                    "̬�Ȼ��������й����ŶԴ˲��е��κη������Ρ�";


    // ���ڲ˵�
    public static void clickMenuAbout(final Context context) {
        String product = colorme_myjsz_name + Colorme.DELIM_LINE
                + colorme_myjsz_version + Colorme.DELIM_LINE
                + colorme_myjsz_catalog;
        String other = zosean_about_company + Colorme.DELIM_LINE
                + zosean_about_tele + Colorme.DELIM_LINE
                + zosean_about_copyright;
        Colorme.clickMenuAbout(context, product, other);
    }

    // ����˵�
    public static void clickMenuMore(final Context context) {
        Colorme.clickMenuMore(context);
    }

    // �˳��˵�
    public static void clickMenuQuit(final Context context) {
        Colorme.clickMenuQuit(context);
    }

}
