package com.aradhana.immerpactnews

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.*
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.view.CameraView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Base64

private const val REQUEST_CODE_PERMISSIONS = 10
private const val HEAD_NAME = 20
private const val CONTENT_NAME = 30
private const val MARQUE_NAME = 40
private const val LOGO_IMAGE = 50
private const val ADD_INFO = 60
private const val LOGO_TEXT = 70

private val REQUIRED_PERMISSIONS =
    arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
private val tag = MainActivity::class.java.simpleName


@SuppressLint("RestrictedApi, ClickableViewAccessibility")

class MainActivity : AppCompatActivity(), LifecycleOwner {

    private var screenDensity: Int = 0
    private var projectManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaRecorder: MediaRecorder? = null
    private var mediaProjectionCallback: MediaProjectionCallback? = null
    //Volume up and down
    //  private  var mediaSesssion:MediaSession = MediaSession(this,"droidagram")

    internal var vidioUri: String = ""

    companion object {
        private val REQUEST_CODE = 1000
        private val REQUEST_PERMISSION = 1001
        private var DISPLAY_WIDTH = 700
        private var DISPLAY_HEIGHT = 700
        private val ORIENTATIONS = SparseIntArray()
        val metrics = DisplayMetrics()
        val GET_TEXT = "get_text"
        val GET_BG = "get_bg"
        val GET_TXT_COL = "get_txt_col"
        val MYPREF = "myPref"
        val HEADNAME_PREF = "head_name_pref"
        val CONTENT_PREF = "content_name_pref"
        val MARQUE_PREF = "marque_name_pref"
        val LOGO__NAME_PREF = "logo_name_pref"
        val LOGO_IMAGE_PREF = "logo_image_pref"
        val HEAD_BG_PREF = "head_bg_pref"
        val HEAD_TXT_COL_PRE = "head_txt_col_pref"
        val CONTENT_BG_PREF = "content_bg_pref"
        val CONTENT_TXT_COL_PREF = "content_txt_col_pref"
        val MARQUE_BG_PREF = "marque_bg_pref"
        val MARQUE_TXT_COL_PREF = "marque_txt_col_pref"
        val LOGO_TXT = "logo_txt"
        val LOGO_BG = "logo_bg"
        val LOGO_TXT_COL = "logo_txt_col"
        val ADD_INFO_TXT = "add_info_txt"
        val ADD_INFO_BG = "add_info_bg"
        val ADD_INFO_TXT_COL = "add_info_txt_col"


        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)


        }
    }

    inner class MediaProjectionCallback : MediaProjection.Callback() {

        override fun onStop() {

            mediaRecorder!!.stop()
            mediaRecorder!!.reset()

            mediaProjection = null
            stopScreenRecord();
        }
    }


    private lateinit var viewFinder: CameraView
    private lateinit var captureButton: ImageButton
    private lateinit var videoCapture: VideoCapture
    private lateinit var marque: TextView
    private lateinit var content: TextView
    private lateinit var heading: TextView
    private lateinit var additional_info: TextView
    private lateinit var logoImage: ImageView
    private lateinit var logoText: TextView
    private lateinit var addtional_info_bg: View
    private lateinit var timeText: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPrefrenceEdit: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        viewFinder = findViewById(R.id.view_finder)
        captureButton = findViewById(R.id.capture_button)
        content = findViewById(R.id.content)
        heading = findViewById(R.id.heading_txt) as TextView
        logoImage = findViewById(R.id.logo_img)
        logoText = findViewById(R.id.logo_name)
        additional_info = findViewById(R.id.breaking_next)
        marque = findViewById(R.id.marque)
        timeText = findViewById(R.id.news_time)
        addtional_info_bg = findViewById(R.id.iv_breaking_background)
        marque.isSelected = true
        val imgResId = R.drawable.news_logo1
        var logoresId = imgResId
        //Volume button controller
        /*mediaSesssion = MediaSession(this.applicationContext,"droidgram")
        mediaSesssion.setCallback(msCalllback)
        mediaSesssion.isActive== true
*/
        //screenRecording code

        windowManager.defaultDisplay.getMetrics(metrics)
        screenDensity = metrics.densityDpi
        mediaRecorder = MediaRecorder()

        projectManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        DISPLAY_HEIGHT = metrics.heightPixels
        DISPLAY_WIDTH = metrics.widthPixels


        sharedPreferences = applicationContext.getSharedPreferences(MYPREF, Context.MODE_PRIVATE)
        sharedPrefrenceEdit = sharedPreferences.edit()
        sharedPrefrenceEdit.apply()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        viewFinder.bindToLifecycle(this)

        heading.setText(sharedPreferences.getString(HEADNAME_PREF, ""))
        heading.setBackgroundColor(
            Color.parseColor(
                sharedPreferences.getString(
                    HEAD_BG_PREF,
                    "#ffffff"
                )
            )
        )
        heading.setTextColor(sharedPreferences.getInt("HEAD_TXT_COL_PRE", Color.BLACK))
        content.setText(sharedPreferences.getString(CONTENT_PREF, ""))
        content.setBackgroundColor(
            Color.parseColor(
                sharedPreferences.getString(
                    CONTENT_BG_PREF,
                    "#000000"
                )
            )
        )
        content.setTextColor(sharedPreferences.getInt("CONTENT_TXT_COL_PREF", Color.WHITE))
        marque.setText(sharedPreferences.getString(MARQUE_PREF, ""))
        marque.setBackgroundColor(
            Color.parseColor(
                sharedPreferences.getString(
                    MARQUE_BG_PREF,
                    "#ffffff"
                )
            )
        )
        marque.setTextColor(sharedPreferences.getInt("MARQUE_TXT_COL_PREF", Color.BLACK))
        logoText.setText(sharedPreferences.getString(LOGO_TXT, ""))
        logoText.setBackgroundColor(
            Color.parseColor(
                sharedPreferences.getString(
                    LOGO_BG,
                    "#000000"
                )
            )
        )
        logoText.setTextColor(sharedPreferences.getInt("LOGO_TXT_COL", Color.WHITE))
        additional_info.setText(sharedPreferences.getString(ADD_INFO_TXT, "Breaking news"))
        additional_info.setBackgroundColor(
            Color.parseColor(
                sharedPreferences.getString(
                    ADD_INFO_BG,
                    "#000000"
                )
            )
        )
        //addtional_info_bg.backgroundTintList(Color.parseColor(sharedPreferences.getString(ADD_INFO_BG,"#000000")))
        additional_info.setTextColor(sharedPreferences.getInt("ADD_INFO_TXT_COL", Color.WHITE))

        // if(logoImage.drawable==null)
        // logoImage.setImageResource(R.drawable.news_logo2)

      /*  logoImage.layout(0, 0, 50, 50);
        logoImage.buildDrawingCache()
        var bitmapPref: Bitmap = Bitmap.createBitmap(logoImage.getDrawingCache())
        var prefByte: ByteArrayOutputStream = ByteArrayOutputStream()
        bitmapPref.compress(Bitmap.CompressFormat.PNG, 100, prefByte)
        var byteArrayPref: ByteArray = prefByte.toByteArray()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var encodePref: String = Base64.getEncoder().encodeToString(byteArrayPref);
            var encodedImage: String? = sharedPreferences.getString("encodedImage", encodePref)
            var decodeByteImg: ByteArray = Base64.getDecoder().decode(encodedImage)
            var decodeImgArray: Bitmap =
                BitmapFactory.decodeByteArray(decodeByteImg, 0, decodeByteImg.size)

            logoImage.setImageBitmap(decodeImgArray)
        } else {
            var encodePref: String =
                android.util.Base64.encodeToString(byteArrayPref, android.util.Base64.DEFAULT);
            var encodedImage: String? = sharedPreferences.getString("encodedImage", encodePref)
            var decodeByteImg: ByteArray = android.util.Base64.decode(
                encodedImage,
                android.util.Base64.DEFAULT
            )
            var decodeImgArray: Bitmap =
                BitmapFactory.decodeByteArray(decodeByteImg, 0, decodeByteImg.size)
            logoImage.setImageBitmap(decodeImgArray)

        }*/


        /*scrn_recrd_btn.setOnClickListener { v ->
            if(ContextCompat.checkSelfPermission(this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)+
                ContextCompat.checkSelfPermission(this@MainActivity,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED )
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity,
                        Manifest.permission.RECORD_AUDIO



                    ))
                {
                    scrn_recrd_btn.isChecked=false
                    Snackbar.make(main_rootLayout,"Permissions", Snackbar.LENGTH_INDEFINITE)
                        .setAction("ENABLE",{
                            ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSION)
                        }).show()
                }
                else{

                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSION)

                }

            }
            else{
                startRecording(v)
            }




        }*/


        // Request camera permissions
       /* if (allPermissionsGranted()) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }*/

        val file = File(
            externalMediaDirs.first(),
            "${System.currentTimeMillis()}.mp4"
        )
        Log.d(tag, "Video location: $file")


      /*  captureButton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                captureButton.setBackgroundColor(Color.GREEN)
                videoCapture.startRecording(file, object : VideoCapture.OnVideoSavedListener {
                    override fun onVideoSaved(file: File?) {
                        Log.d(tag, "Video File : $file")
                    }

                    override fun onError(
                        useCaseError: VideoCapture.UseCaseError?,
                        message: String?,
                        cause: Throwable?
                    ) {
                        Log.d(tag, "Video Error: $message")
                    }
                })

            } else if (event.action == MotionEvent.ACTION_UP) {
                captureButton.setBackgroundColor(Color.RED)
                videoCapture.stopRecording()
                Log.d(tag, "Video File stopped")
            }
            false
        }
*/
        /*logoImage.setTag(R.drawable.news_logo1)





        logoImage.setOnClickListener{
            val imgResId = R.drawable.ic_launcher_background
            var resId = imgResId

        }*/

        /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE){
             val current = LocalTime.now()
             val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
             val formatted = current.format(formatter)
             timeText.setText(formatted.toString())
         }*/


        val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        timeText.setText(currentTime)

        breaking_next.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        breaking_next.startAnimation(animation)

        /* val anim = AnimationUtils.loadAnimation(this,R.anim.fade_out)
         breaking_next.startAnimation(anim)
         Handler().postDelayed({
             breaking_next.visibility = View.GONE
         }, 1000)*/


        heading.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var request: Intent = Intent(applicationContext, edit_layout::class.java)
                request.putExtra(GET_TEXT, heading.getText())
                var bg_color: ColorDrawable = heading.getBackground() as ColorDrawable
                var bg_id: Int = bg_color.getColor()
                var hex_color: String = String.format("#%06X", 0xFFFFFF and bg_id)

                request.putExtra(GET_BG, hex_color)
                request.putExtra(GET_TXT_COL, heading.currentTextColor);


                /* var bg_color_txt:ColorDrawable = heading.getTextColors() as ColorDrawable
                 var bg_id_txt :Int = bg_color.getColor()
                 var hex_color_txt:String= String.format("#%06X", 0xFFFFFF and bg_id)

                 request.putExtra(GET_TXT_COL,hex_color_txt)*/



                startActivityForResult(request, HEAD_NAME)


            }


        })

        content.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var request: Intent = Intent(applicationContext, edit_layout::class.java)
                request.putExtra(GET_TEXT, content.getText())


                var bg_color: ColorDrawable = content.getBackground() as ColorDrawable
                var bg_id: Int = bg_color.getColor()
                var hex_color: String = String.format("#%06X", 0xFFFFFF and bg_id)

                request.putExtra(GET_BG, hex_color)
                request.putExtra(GET_TXT_COL, content.currentTextColor);
                startActivityForResult(request, CONTENT_NAME)
            }
        })

        marque.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var request: Intent = Intent(applicationContext, edit_layout::class.java)
                request.putExtra(GET_TEXT, marque.getText())


                var bg_color: ColorDrawable = marque.getBackground() as ColorDrawable
                var bg_id: Int = bg_color.getColor()
                var hex_color: String = String.format("#%06X", 0xFFFFFF and bg_id)

                request.putExtra(GET_BG, hex_color)
                request.putExtra(GET_TXT_COL, marque.currentTextColor);


                startActivityForResult(request, MARQUE_NAME)
            }
        })

        logoImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var logorequest: Intent = Intent(applicationContext, LogoEdit::class.java)

                logoImage.buildDrawingCache()
                 var logoBitmap: Bitmap = logoImage.getDrawingCache()
                 var  byteArrayStream : ByteArrayOutputStream =  ByteArrayOutputStream()
                 logoBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayStream)
                 var byteArray :ByteArray = byteArrayStream.toByteArray()
                logorequest.putExtra("LOGO_IMAGE_PREF", byteArray)
                startActivityForResult(logorequest, LOGO_IMAGE)

            }
        })

        logoText.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var request: Intent = Intent(applicationContext, edit_layout::class.java)
                request.putExtra(GET_TEXT, logoText.getText())


                var bg_color: ColorDrawable = logoText.getBackground() as ColorDrawable
                var bg_id: Int = bg_color.getColor()
                var hex_color: String = String.format("#%06X", 0xFFFFFF and bg_id)

                request.putExtra(GET_BG, hex_color)
                request.putExtra(GET_TXT_COL, logoText.currentTextColor);
                startActivityForResult(request, LOGO_TEXT)


            }
        })

        additional_info.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var request: Intent = Intent(applicationContext, edit_layout::class.java)
                request.putExtra(GET_TEXT, additional_info.getText())


                var bg_color: ColorDrawable = additional_info.getBackground() as ColorDrawable
                var bg_id: Int = bg_color.getColor()
                var hex_color: String = String.format("#%06X", 0xFFFFFF and bg_id)

                request.putExtra(GET_BG, hex_color)
                request.putExtra(GET_TXT_COL, marque.currentTextColor);


                startActivityForResult(request, ADD_INFO)
            }
        })


    }

    override fun onRequestPermissionsResult(                                         // both  for screen recording and  vidio recording
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    startRecording()
                else {

                    Snackbar.make(main_rootLayout, "Permissions", Snackbar.LENGTH_INDEFINITE)
                        .setAction("ENABLE", {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_SETTINGS
                            intent.addCategory(Intent.CATEGORY_DEFAULT)
                            intent.data = Uri.parse("package: $packageName")
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                            startActivity(intent)


                        }).show()
                }
                return

            }
        }
       /* if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }*/
    }

    //screen recording
    private fun startRecording() {

        initRecorder()
        shareScreen()


    }

    private fun shareScreen() {
        if (mediaProjection == null) {
            startActivityForResult(projectManager!!.createScreenCaptureIntent(), REQUEST_CODE)
            return
        }
        virtualDisplay = createVirtualDisplay()
        mediaRecorder!!.start()
    }

    private fun createVirtualDisplay(): VirtualDisplay? {
        return mediaProjection?.createVirtualDisplay(
            "MainActivity", DISPLAY_WIDTH, DISPLAY_HEIGHT, screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder!!.surface, null, null
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == HEAD_NAME || requestCode == CONTENT_NAME || requestCode == MARQUE_NAME
                    || requestCode == LOGO_TEXT || requestCode == ADD_INFO) && resultCode == Activity.RESULT_OK
        ) {


            var getName: String = data!!.getStringExtra(edit_layout.NAME_)
            var getColor_: String = data!!.getStringExtra(edit_layout.COLOR_)
            var getTextColor: Int = data!!.getIntExtra(edit_layout.COLOR_TXT, 0)
            Log.d(tag, "getname" + getName)

            Toast.makeText(this, getName, Toast.LENGTH_SHORT).show()
            Toast.makeText(this, getColor_, Toast.LENGTH_SHORT).show()


            if (requestCode == HEAD_NAME) {
                var type: Int = 5
                setData(getName, getColor_, getTextColor, type)
            }
            if (requestCode == CONTENT_NAME) {
                var type: Int = 6
                setData(getName, getColor_, getTextColor, type)
            }
            if (requestCode == MARQUE_NAME) {
                var type: Int = 7
                setData(getName, getColor_, getTextColor, type)
            }

            if (requestCode == LOGO_TEXT) {
                var type: Int = 9
                setData(getName, getColor_, getTextColor, type)
            }
            if (requestCode == ADD_INFO) {
                var type: Int = 10
                setData(getName, getColor_, getTextColor, type)
            }


            return


            // The user picked a contact.
            // The Intent's data Uri identifies which contact was selected.


        }

        if (requestCode == LOGO_IMAGE && resultCode == Activity.RESULT_OK) {

            var imgByte: ByteArray = data!!.getByteArrayExtra(LogoEdit.IMAGE_BITMAP_VALUE)
            setImage(imgByte)

        }

        if (requestCode != REQUEST_CODE) {
            return
        }



        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "Screen cast Permission denied", Toast.LENGTH_LONG).show()
            return
        }

        Log.d(tag,"data value --"+data)
       /* var screenIntent:Intent = projectManager!!.createScreenCaptureIntent()
        startForegroundService(screenIntent)*/
        mediaProjectionCallback = MediaProjectionCallback()
        mediaProjection = projectManager?.getMediaProjection(resultCode, data!!) as MediaProjection
        mediaProjection!!.registerCallback(mediaProjectionCallback, null)
        virtualDisplay = createVirtualDisplay()
        mediaRecorder!!.start()
    }

    public fun setData(getName: String, getColor_: String, getColorText: Int, type: Int) {
        var txt_num_val: Int
        if (type == 6) {
            content.text = getName
            content.setBackgroundColor(Color.parseColor(getColor_))
            content.setTextColor(getColorText);
            sharedPrefrenceEdit.putString(CONTENT_PREF, getName)

            sharedPrefrenceEdit.putString(CONTENT_BG_PREF, getColor_)
            sharedPrefrenceEdit.putInt("CONTENT_TXT_COL_PREF", getColorText)



            sharedPrefrenceEdit.commit()


        } else if (type == 5) {
            heading.text = getName
            heading.setBackgroundColor(Color.parseColor(getColor_))
            heading.setTextColor(getColorText)
            sharedPrefrenceEdit.putString(HEADNAME_PREF, getName)
            sharedPrefrenceEdit.putString(HEAD_BG_PREF, getColor_)
            sharedPrefrenceEdit.putInt("HEAD_TXT_COL_PRE", getColorText)


            sharedPrefrenceEdit.commit()
        } else if (type == 7) {
            marque.text = getName
            marque.setBackgroundColor(Color.parseColor(getColor_))
            marque.setTextColor(getColorText)
            sharedPrefrenceEdit.putString(MARQUE_PREF, getName)
            sharedPrefrenceEdit.putString(MARQUE_BG_PREF, getColor_)
            sharedPrefrenceEdit.putInt("MARQUE_TXT_COL_PREF", getColorText)


            sharedPrefrenceEdit.commit()

        } else if (type == 9) {
            logoText.text = getName
            logoText.setBackgroundColor(Color.parseColor(getColor_))
            logoText.setTextColor(getColorText)
            sharedPrefrenceEdit.putString(LOGO_TXT, getName)
            sharedPrefrenceEdit.putString(LOGO_BG, getColor_)
            sharedPrefrenceEdit.putInt("LOGO_TXT_COL", getColorText)


            sharedPrefrenceEdit.commit()

        } else if (type == 10) {
            additional_info.text = getName
            additional_info.setBackgroundColor(Color.parseColor(getColor_))
            // addtional_info_bg.setBackgroundColor(Color.parseColor(getColor_))
            additional_info.setTextColor(getColorText)
            sharedPrefrenceEdit.putString(ADD_INFO_TXT, getName)
            sharedPrefrenceEdit.putString(ADD_INFO_BG, getColor_)
            sharedPrefrenceEdit.putInt("ADD_INFO_TXT_COL", getColorText)


            sharedPrefrenceEdit.commit()

        }


    }


    private fun setImage(imgByte: ByteArray) {

        var logoBitmap: Bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
        logoImage.setImageBitmap(logoBitmap)

        var encode: String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encode = Base64.getEncoder().encodeToString(imgByte);
        } else {
            encode = android.util.Base64.encodeToString(imgByte, android.util.Base64.DEFAULT);
        }
/*
        Log.d(tag, "encodedStr$encode")
        Toast.makeText(this,"encodedStr$encode",Toast.LENGTH_SHORT)*/


        //Log.d(tag,"String$encode")
        sharedPrefrenceEdit.putString("encodedImage", encode)

    }

    private fun initRecorder() {
        try {
            val camcorderProfile: CamcorderProfile =
                CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)   //MediaRecorder.AudioSource.VOICE_CALL
            mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)               //camcorderProfile.fileFormat

            // mediaRecorder!!.setAudioChannels(camcorderProfile.audioChannels);
            // mediaRecorder!!.setAudioSamplingRate(camcorderProfile.audioSampleRate);
            //camcorderProfile.videoFrameHeight = metrics.heightPixels
            // camcorderProfile.videoFrameWidth = metrics.widthPixels
            // mediaRecorder!!.setProfile(profile)
            vidioUri =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + StringBuilder(
                    "/"
                )
                    .append("News Maker")
                    .append(SimpleDateFormat("dd-MM-yyyy-hh_mm_ss").format(Date()))
                    .append(".mp4")
                    .toString()

            mediaRecorder!!.setOutputFile(vidioUri)
            mediaRecorder!!.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT)
            mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)     //camcorderProfile.audioCodec
            mediaRecorder!!.setVideoEncodingBitRate(camcorderProfile.videoBitRate)                      //512*1000          //camcorderProfile.videoBitRate
            mediaRecorder!!.setVideoFrameRate(camcorderProfile.videoFrameRate)               //30       //camcorderProfile.videoFrameRate

            val rotation = windowManager.defaultDisplay.rotation
            val orientations = ORIENTATIONS.get(rotation +90)
            mediaRecorder!!.setOrientationHint(orientations)
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d(tag, "Screen vidio file : $vidioUri")
    }

    private fun stopScreenRecord() {
        if (virtualDisplay == null)
            return
        virtualDisplay!!.release()
        destroyMediaProjection()
    }

    private fun destroyMediaProjection() {
        if (mediaProjection != null) {
            mediaProjection!!.unregisterCallback(mediaProjectionCallback)
            mediaProjection!!.stop()
            mediaProjection = null

            intent = Intent(this, sharePlay::class.java)
            intent.putExtra("file location", vidioUri)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyMediaProjection()
    }


    //stop recording

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    //start camera
    /* fun startCamera() {

             doAsync {

                 CameraX.unbindAll()
                 val aspectRatio =
                     Rational(viewFinder.getWidth(), viewFinder.getHeight())
                 val screen =
                     Size(viewFinder.getWidth(), viewFinder.getHeight())

                 val pConfig = PreviewConfig.Builder()
                     .setTargetAspectRatio(aspectRatio)
                     .setTargetResolution(screen) //.setLensFacing(CameraX.LensFacing.FRONT)
                     .build()

                 // Build the viewfinder use case
                 val preview = Preview(pConfig)

                 Log.d(tag, "preview $preview")

                 preview.setOnPreviewOutputUpdateListener {
                     viewFinder.surfaceTexture = it.surfaceTexture
                     updateTransform()
                 }

                 // Create a configuration object for the video use case
                 val videoCaptureConfig = VideoCaptureConfig.Builder().apply {
                     setTargetRotation(viewFinder.display.rotation)
                 }.build()
                 videoCapture = VideoCapture(videoCaptureConfig)


                 // Bind use cases to lifecycle
                 CameraX.bindToLifecycle(this@MainActivity, preview, videoCapture)
                 Log.d(tag,"asynctask")

             }





    }

    private fun updateTransform() {

        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.measuredWidth/ 2f
        val centerY = viewFinder.measuredHeight / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }

        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }
*/

    public override fun dispatchKeyEvent(keyevent: KeyEvent): Boolean {
        var action: Int
        var keyCode: Int

        action = keyevent.getAction()
        keyCode = keyevent.getKeyCode()


        when (keyCode) {

            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (KeyEvent.ACTION_DOWN == action) {
                    mediaRecorder!!.stop()
                    mediaRecorder!!.reset()
                    stopScreenRecord()

                    //play vidio in vidio view
                    vidio_view.visibility = View.VISIBLE
                    vidio_view.setVideoURI(Uri.parse(vidioUri))
                    vidio_view.start()
                    Log.d(tag, "volumedown")


                }
                return true


            }


            KeyEvent.KEYCODE_VOLUME_UP -> {
                if (KeyEvent.ACTION_DOWN == action) {
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) +
                        ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.RECORD_AUDIO
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this@MainActivity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                this@MainActivity,
                                Manifest.permission.RECORD_AUDIO


                            )
                        ) {

                            Snackbar.make(
                                main_rootLayout,
                                "Permissions",
                                Snackbar.LENGTH_INDEFINITE
                            )
                                .setAction("ENABLE", {
                                    ActivityCompat.requestPermissions(
                                        this@MainActivity,
                                        arrayOf(
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.RECORD_AUDIO
                                        ), REQUEST_PERMISSION
                                    )
                                }).show()
                        } else {

                            ActivityCompat.requestPermissions(
                                this@MainActivity,
                                arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO
                                ), REQUEST_PERMISSION
                            )

                        }

                    } else {
                        startRecording()
                    }




                    Log.d(tag, "volumeup")

                }
                return true


            }

            else -> {
                Log.d(tag, "nothing selected")
            }
        }

        return super.dispatchKeyEvent(keyevent)
    }

    override public  fun onConfigurationChanged(newConfig:Configuration){
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            viewFinder .setRotation(90.toFloat());
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            viewFinder.setRotation(0.toFloat());
        }
    }



    /* override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
         return super.onKeyDown(keyCode, event)
         when (keyCode) {
             KeyEvent.KEYCODE_VOLUME_UP -> {
                 Log.d(tag, "volumeup")


             }

             KeyEvent.KEYCODE_VOLUME_DOWN -> {
                 Log.d(tag, "volumedown")


             }
         }
         return super.onKeyDown(keyCode, event)

     }
 */


    /* fun ColorPicker() {
         ColorPickerDialog
             .Builder(this)                    // Pass Activity Instance
             .setColorShape(ColorShape.CIRCLE)   // Default ColorShape.CIRCLE
             .setDefaultColor(R.color.colorPrimary)            // Pass Default Color
             .setColorListener { color, colorHex ->
                 // Handle Color Selection
             }
             .show()


     }*/


}