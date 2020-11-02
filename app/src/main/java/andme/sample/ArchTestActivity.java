package andme.sample;

import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

import andme.arch.app.AMActivity;
import andme.arch.app.AMViewModel;

/**
 * Created by Lucio on 2020-11-02.
 */
public class ArchTestActivity extends AMActivity<AMViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel();
    }
}
