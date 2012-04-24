package df.util.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.waps.AdView;
import com.waps.AppConnect;
import df.util.app.DooyogameMntsWaps;


/**
 * Created by IntelliJ IDEA.
 * User: nalone
 * Date: 12-3-21
 * Time: ÏÂÎç4:04
 * To change this template use File | Settings | File Templates.
 */
public class TestActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.df_test_main);


        DooyogameMntsWaps.showWapsAdView(this);

//        int level_1 = 1000;
//        DooyogameMntsWaps.runNextLevel(this, level_1, null);
//        int level_2 = 1001;
//        DooyogameMntsWaps.runNextLevel(this, level_2, null);
        int level_3 = 1002;
//        DooyogameMntsWaps.runNextLevel(this, level_3, null);
//        int level_4 = 1003;
//        DooyogameMntsWaps.runNextLevel(this, level_4, null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.

        AppConnect.getInstance(this).finalize();

    }
}