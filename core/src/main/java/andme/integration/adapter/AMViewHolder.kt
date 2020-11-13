package andme.integration.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by Lucio on 2020-11-12.
 */
open class AMViewHolder(view: View) : BaseViewHolder(view){

    constructor(inflater: LayoutInflater, @LayoutRes layoutId: Int, parent: ViewGroup?)
            : this(inflater.inflate(layoutId, parent, false))
}