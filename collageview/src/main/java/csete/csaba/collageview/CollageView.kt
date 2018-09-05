package csete.csaba.collageview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView


class CollageView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var image1: ImageView
    private var image2: ImageView
    private var image3: ImageView
    private var moreImagesText: TextView

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    private val scale: Float = resources.displayMetrics.density
    private val margin = (scale * 4).toInt()
    private val collageViewItems = ArrayList<CollageViewItem>()

    private lateinit var callback: OnImagePlaced
    private var imageCount: Int = 0

    init {
        inflate(context, R.layout.layout_collage, this)
        image1 = findViewById(R.id.image1)
        image2 = findViewById(R.id.image2)
        image3 = findViewById(R.id.image3)
        moreImagesText = findViewById(R.id.moreImagesText)

        image1.setOnClickListener { callback.onImageClicked(0) }
        image2.setOnClickListener { callback.onImageClicked(1) }
        image3.setOnClickListener { callback.onImageClicked(2) }
        moreImagesText.setOnClickListener { callback.onImageClicked(2) }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            viewWidth = right - left
            viewHeight = bottom - top
            positionViews()
        }
    }

    fun setImagePlacedCallback(callback: OnImagePlaced) {
        this.callback = callback
    }

    fun setImageUrls(imageUrlList: List<String>) {
        if (imageUrlList.isEmpty()) {
            return
        }
        imageCount = imageUrlList.size
        if (imageUrlList.isNotEmpty()) {
            collageViewItems.add(CollageViewItem(image1, imageUrlList[0]))
        }
        if (imageUrlList.size > 1) {
            collageViewItems.add(CollageViewItem(image2, imageUrlList[1]))
        }
        if (imageUrlList.size > 2) {
            collageViewItems.add(CollageViewItem(image3, imageUrlList[2]))
        }

        collageViewItems.forEach { callback.onImagePlaced(it.imageView, it.imageUri) }
        invalidate()
    }

    private fun positionViews() {
        Log.d("PositioningViews", "" + collageViewItems.count())
        when (collageViewItems.count()) {
            1 -> {
                positionBigImage()
            }
            2 -> {
                positionTwoImages()
            }
            else -> {
                positionAllViews(imageCount > 3)
            }
        }
    }

    private fun positionBigImage() {
        Log.d("PositioningViews", "positionBigImage")
        val bigWidth = viewWidth - (2 * margin)
        val bigHeight = viewHeight - (2 * margin)
        positionView(image1, bigWidth, bigHeight, margin, margin, 0, 0)
    }

    private fun positionTwoImages() {
        Log.d("PositioningViews", "positionTwoImages")
        val width = (viewWidth - (3 * margin)) / 2
        val height = viewHeight - (2 * margin)
        positionView(image1, width, height, margin, margin, 0, 0)
        positionView(image2, width, height, width + (2 * margin), margin, 0, 0)
    }

    private fun positionAllViews(includeText: Boolean) {
        Log.d("PositioningViews", "positionAllViews: $includeText")
        val smallSide = (viewHeight - (3 * margin)) / 2
        val bigWidth = viewWidth - (3 * margin) - smallSide
        val bigHeight = viewHeight - (2 * margin)
        positionView(image1, bigWidth, bigHeight, margin, margin, 0, 0)
        positionView(image2, smallSide, smallSide, bigWidth + (2 * margin), margin, 0, 0)
        positionView(image3, smallSide, smallSide, bigWidth + (2 * margin), smallSide + (2 * margin), 0, 0)
        if (includeText) {
            moreImagesText.visibility = View.VISIBLE
            moreImagesText.text = resources.getString(R.string.more_images_plus, imageCount - 2)
            positionView(moreImagesText, smallSide, smallSide, bigWidth + (2 * margin), smallSide + (2 * margin), 0, 0)
        } else {
            moreImagesText.visibility = View.GONE
        }
    }

    private fun positionView(view: View, width: Int, height: Int, marginLeft: Int, marginTop: Int, marginRight: Int, marginBottom: Int) {
        val layoutParams: FrameLayout.LayoutParams = view.layoutParams as LayoutParams
        layoutParams.width = width
        layoutParams.height = height
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
    }

    private data class CollageViewItem(
            val imageView: ImageView,
            val imageUri: String
    )

    interface OnImagePlaced {

        /**
         * Load image into view with your favorite image loader.
         */
        fun onImagePlaced(imageView: ImageView, imageUri: String)

        fun onImageClicked(position: Int)
    }
}
