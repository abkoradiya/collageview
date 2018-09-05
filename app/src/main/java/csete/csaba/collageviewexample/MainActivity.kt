package csete.csaba.collageviewexample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import csete.csaba.collageview.CollageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), CollageView.OnImagePlaced {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uri1 = "https://i.imgur.com/OTaPKFe.jpg"
        val uri2 = "https://i.imgur.com/UZI7oNO.jpg"
        val uri3 = "https://i.imgur.com/YT039I5.jpg"
        val uri4 = "https://i.imgur.com/kjr70C6.jpg"
        val uri5 = "https://i.imgur.com/IvGJtd2.jpg"

        imageCount1.setImagePlacedCallback(this)
        imageCount2.setImagePlacedCallback(this)
        imageCount3.setImagePlacedCallback(this)
        imageCount4.setImagePlacedCallback(this)
        imageCount5.setImagePlacedCallback(this)

        imageCount1.setImageUrls(listOf(uri1))
        imageCount2.setImageUrls(listOf(uri2, uri1))
        imageCount3.setImageUrls(listOf(uri3, uri2, uri1))
        imageCount4.setImageUrls(listOf(uri4, uri3, uri2, uri1))
        imageCount5.setImageUrls(listOf(uri5, uri4, uri3, uri2, uri1))
    }

    override fun onImagePlaced(imageView: ImageView, imageUri: String) {
        Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions.centerCropTransform())
                .into(imageView)
    }

    override fun onImageClicked(position: Int) {
        Log.d("onImageClicked", "$position")
    }

}
