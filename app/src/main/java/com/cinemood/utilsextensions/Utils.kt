package com.cinemood.utilsextensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.Fragment
import android.util.Log.d
import android.widget.ImageView
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * picasso or glide load fun
 */
fun loadImageWithUrl(imgUrl: String?, iv: ImageView, context: Context = App.appComponent.context()) {
    Glide.with(context)
            .load("https://iqsha.ru$imgUrl")
            .asBitmap()
            .placeholder(R.drawable.stub)
            .error(R.drawable.stub)
            .transform(CircleTransform(context))
            .fitCenter()
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    d("glide resource ready: $resource")
                    iv.setImageBitmap(resource)
                }
            })
}

/**
 * get drawable of bitmap from assets
 */
fun getDrawableFromAsset(context: Context, filePath: String): Bitmap? {
    try {
        val ims = context.assets.open(filePath)
        //val d = Drawable.createFromStream(ims, null)
        val d = BitmapFactory.decodeStream(ims)
        ims.close()
        return d
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
}

/**
 * events
 */
fun showErrorEvent(errorMsg: String) {
    DeviceEvents.Companion.getInstance().logEvent(
            DeviceEvent("ApplicationEvent")
                    .putCustomAttribute("backend service", "error")
                    .putCustomAttribute("error message", errorMsg)
    )
}

/**
 * dialogs
 */
fun showProgressDialog(context: Context) =
        NotificationDialog.showProgressDialog(context,
                context.getString(R.string.loading),
                context.getString(R.string.please_wait))

/**
 * fragment creator with bundle from anko
 */
inline fun <reified T : Fragment> newInstanceFragment(vararg params: Pair<String, Any>) =
        T::class.java.newInstance().apply {
            arguments = bundleOf(*params)
        }!!

/**
 * get typeface
 */
fun getOpenSansBold() = FontsLoader.loadFromSystem(FontsLoader.Font.OpenSansBold)

fun getOpenSansRegular() = FontsLoader.loadFromSystem(FontsLoader.Font.OpenSansRegular)

/**
 * rx schedulers
 */
fun <T> applySchedulers(): Observable.Transformer<T, T> {
    return Observable.Transformer {
        it.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted {
                    d("rx onCompleted")
                }
    }
}

fun getCurrentTime(): String {
    val currentTime = Calendar.getInstance().time
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    return df.format(currentTime)
}

fun getUserAgent(context: Context) =
        Build.PRODUCT +
                "/" +
                DeviceManager.getDeviceSerialNumber() +
                "/" +
                DeviceManager.getFirmwareVersion(context) +
                "; " +
                context.packageName +
                "/" +
                DeviceManager.getApplicationVersion(context)
