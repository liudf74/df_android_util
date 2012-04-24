package df.util.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.*;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-8-18
 * Time: ����4:20
 * To change this template use File | Settings | File Templates.
 */
public class SmsUtil {

    public static final String TAG = "df.util.SmsUtil";

    public static final String SMS_SENT = "SMS_SENT";
    public static final String SMS_DELIVERED = "SMS_DELIVERED";

    //
    public static final String HINT_SMS_FAIL = "����ʧ�ܣ������ԡ�";

    // todo: ʵ�ʴ�������Ҫ����
//    public static final int STR_SMS_MO_ADDRESS     = 0; //R.string.sms_mo_address;
//    public static final int STR_SMS_MO_CONTENT     = 0; // R.string.sms_mo_content;
//    public static final int STR_SMS_RESULT_ERROR   = 0; // R.string.sms_result_error;
//    public static final int STR_SMS_HINT_TITLE     = 0; // R.string.sms_hint_title;
//    public static final int STR_SMS_HINT_MESSAGE   = 0; // R.string.sms_hint_message;
//    public static final int STR_SMS_BUTTON_CANCEL  = 0; // R.string.sms_button_cancel;
//    public static final int STR_SMS_BUTTON_CONFIRM = 0; // R.string.sms_button_confirm;

    // ���ŷ���״̬�仯�ص��ӿ�
    public static interface OnSmsStatusChangeListener {
        enum STATUS {
            cancel,     // �û�ȡ�����ͣ�����ʧ��״̬��
            sending,    // ���ڷ�����
            sent,       // �Ѿ�����
            delivered,  // �Է��Ѿ��յ������ճɹ�״̬��
            error,      // ����ʧ�ܣ�����ʧ��״̬��
        }

        public abstract void onSmsStatusChanged(STATUS status);
    }

    /**
     * �û��������Ͷ���
     *
     * @param context
     */
//    public static void sendTextSmsByUser(Context context) {
//        String toAddress = context.getString(STR_SMS_MO_ADDRESS);
//        String msgContent = context.getString(STR_SMS_MO_CONTENT);
//        Log.i(TAG, "sendTextSmsByUser, toAddress = " + toAddress + ", msgContent = " + msgContent);
//        Uri uri = Uri.parse("smsto:" + toAddress);
//        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
//        sendIntent.putExtra("sms_body", msgContent);
//        context.startActivity(sendIntent);
//    }

    /**
     * ϵͳֱ�ӷ��Ͷ���
     *
     * @param smsAddress
     * @param smsContent
     * @param context
     */
    public static void sendTextSmsBySystem(final Context context,
                                           final SmsUtil.OnSmsStatusChangeListener paidListener,
                                           final String smsAddress,
                                           final String smsContent) {
        String toAddress = smsAddress;
        String msgContent = smsContent;
        Log.i(TAG, "sendTextSmsBySystem, toAddress = " + toAddress + ", msgContent = " + msgContent);
        SmsManager sms = SmsManager.getDefault();

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(SMS_DELIVERED), 0);

        //�����ŷ��ͺ�Ĵ���
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                int i = getResultCode();
                if (i == Activity.RESULT_OK) {
//                    Toast.makeText(context, context.getString(R.string.sms_result_sent), Toast.LENGTH_SHORT).show();
                    if (paidListener != null) {
                        paidListener.onSmsStatusChanged(OnSmsStatusChangeListener.STATUS.sent);
                    }
                } else {
                    String s = "";
                    if (i == SmsManager.RESULT_ERROR_GENERIC_FAILURE)
                        s = "Generic failure";
                    if (i == SmsManager.RESULT_ERROR_NO_SERVICE)
                        s = "No service";
                    if (i == SmsManager.RESULT_ERROR_NULL_PDU)
                        s = "Null PDU";
                    if (i == SmsManager.RESULT_ERROR_RADIO_OFF)
                        s = "Radio off";
                    Log.d(TAG, "sms send error, code = " + i + ", result = " + s);

                    Toast.makeText(context, HINT_SMS_FAIL, Toast.LENGTH_LONG).show();
                    if (paidListener != null) {
                        paidListener.onSmsStatusChanged(OnSmsStatusChangeListener.STATUS.error);
                    }
                }
            }
        }, new IntentFilter(SMS_SENT));

        //�������Ѵ������
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                int i = getResultCode();
                if (i == Activity.RESULT_OK) {
//                    Toast.makeText(context, context.getString(R.string.sms_result_delivered), Toast.LENGTH_SHORT).show();
                    if (paidListener != null) {
                        paidListener.onSmsStatusChanged(OnSmsStatusChangeListener.STATUS.delivered);
                    }
                } else {

                    String s = "";
                    if (i == Activity.RESULT_CANCELED)
                        s = "SMS not delivered";
                    Log.d(TAG, "sms deliver error, code = " + i + ", result = " + s);

                    Toast.makeText(context, HINT_SMS_FAIL, Toast.LENGTH_LONG).show();
                    if (paidListener != null) {
                        paidListener.onSmsStatusChanged(OnSmsStatusChangeListener.STATUS.error);
                    }
                }
            }
        }, new IntentFilter(SMS_DELIVERED));

        // ���Ͷ���
        sms.sendTextMessage(toAddress, null, msgContent, sentPI, deliveredPI);
    }

    /**
     * ��ʾ�û�Ҫ�۷�
     *
     * @param context
     * @param paidListener
     */
//    public static void promtSmsPay(final Context context, final SmsUtil.OnSmsStatusChangeListener paidListener) {
//        final AlertDialog dialog = new AlertDialog.Builder(context)
//                .setCancelable(false)
//                .setTitle(context.getString(STR_SMS_HINT_TITLE))
//                .setMessage(context.getString(STR_SMS_HINT_MESSAGE))
//                .setNegativeButton(context.getString(STR_SMS_BUTTON_CANCEL), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Log.i(TAG, "pay canceled by user");
//                        if (paidListener != null) {
//                            paidListener.onSmsStatusChanged(OnSmsStatusChangeListener.STATUS.cancel);
//                        }
//                    }
//                })
//                .setPositiveButton(context.getString(STR_SMS_BUTTON_CONFIRM), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Log.i(TAG, "pay confirmed by user");
//                        sendTextSmsBySystem(context, paidListener, context.getString(STR_SMS_MO_ADDRESS), context.getString(STR_SMS_MO_CONTENT));
//                        if (paidListener != null) {
//                            paidListener.onSmsStatusChanged(OnSmsStatusChangeListener.STATUS.sending);
//                        }
//                    }
//                })
//                .create();
//        dialog.show();
//    }

}
