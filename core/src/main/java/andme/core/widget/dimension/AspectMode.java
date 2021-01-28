package andme.core.widget.dimension;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 比例模式
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({AspectMode.BASED_ON_WIDTH, AspectMode.BASED_ON_HEIGHT})
public @interface AspectMode {
    int BASED_ON_WIDTH = 0;
    int BASED_ON_HEIGHT = 1;
}
