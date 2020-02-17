package com.aradhana.immerpactnews

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.*
import de.hdodenhof.circleimageview.CircleImageView

private val tag = edit_layout::class.java.simpleName

private lateinit var edit_head: EditText
private  lateinit var bg_col: TextView
private  lateinit var txt_col: TextView
private  lateinit var  edit_txt: TextView


private lateinit var ok: ImageButton
private lateinit var red_: CircleImageView
private lateinit var black_: CircleImageView
private lateinit var white_: CircleImageView
private lateinit var yellow_: CircleImageView
private lateinit var blue_: CircleImageView
private lateinit var grey_: CircleImageView
private lateinit var pink_: CircleImageView
private lateinit var green_: CircleImageView

private  lateinit var bg_Color: ColorDrawable
private   var colorId:Int =0
private  var  hexColor_:String =""
private lateinit var red__txt: CircleImageView
private lateinit var black_txt: CircleImageView
private lateinit var white_txt: CircleImageView
private lateinit var yellow_txt_: CircleImageView
private lateinit var blue_txt: CircleImageView
private lateinit var grey_txt: CircleImageView
private lateinit var pink_txt: CircleImageView
private lateinit var green_txt: CircleImageView

private  lateinit var bg_color_txt: ColorDrawable
private   var colorId_txt:Int =0
private  var  hexColor_txt:String =""

/*private  lateinit var  logo_first :ImageView
private lateinit var  logo_second : ImageView
private  var resID :Int = 0*/



//private lateinit var demo_: TextView










class edit_layout : AppCompatActivity() {

    companion object {
        val NAME_ = "name"
        val COLOR_ = "color"
        val COLOR_TXT ="color_txt"
        val IMG_ID ="image_id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_edit_layout)

        edit_head = findViewById(R.id.edit_name)

        ok = findViewById(R.id.btn_ok)
        red_ = findViewById(R.id.red_col)
        blue_ = findViewById(R.id.blue_col)
        black_ = findViewById(R.id.black_col)
        white_ = findViewById(R.id.white_col)
        grey_ = findViewById(R.id.grey_col)
        yellow_ = findViewById(R.id.yellow_col)
        pink_ = findViewById(R.id.pink_col)
        green_ = findViewById(R.id.green_col)


        red__txt= findViewById(R.id.red_txt_col)
        blue_txt= findViewById(R.id.blue_txt_col)
        black_txt= findViewById(R.id.black_txt_col)
        white_txt= findViewById(R.id.white_txt_col)
        yellow_txt_= findViewById(R.id.yellow_txt_col)
        grey_txt = findViewById(R.id.grey_txt_col)
        pink_txt= findViewById(R.id.pink_txt_col)
        green_txt= findViewById(R.id.green_txt_col)

        bg_col = findViewById(R.id.bg_text)
        txt_col = findViewById(R.id.txt_col_txt)
        edit_txt = findViewById(R.id.editTxt_text)


        /* logo_first = findViewById(R.id.first_logo)
         logo_second = findViewById(R.id.second_logo)*/

        // demo_ = findViewById(R.id.demo_view)





        red_.setOnClickListener {

            bg_Color = red_.getBackground() as ColorDrawable
            colorId = bg_Color.getColor()
            Toast.makeText(this, "color+ $colorId", Toast.LENGTH_SHORT).show()
            hexColor_ = String.format("#%06X", 0xFFFFFF and colorId)
            Toast.makeText(this, "color+ $hexColor_", Toast.LENGTH_SHORT).show()
            edit_head.setBackgroundColor(Color.parseColor(hexColor_))






        }

        blue_.setOnClickListener {
            bg_Color = blue_.getBackground() as ColorDrawable
            colorId = bg_Color.getColor()
            Toast.makeText(this, "color+ $colorId", Toast.LENGTH_SHORT).show()
            hexColor_ = String.format("#%06X", 0xFFFFFF and colorId)
            Toast.makeText(this, "color+ $hexColor_", Toast.LENGTH_SHORT).show()
            edit_head.setBackgroundColor(Color.parseColor(hexColor_))



        }
        black_.setOnClickListener {
            bg_Color = black_.getBackground() as ColorDrawable
            colorId = bg_Color.getColor()
            Toast.makeText(this, "color+ $colorId", Toast.LENGTH_SHORT).show()
            hexColor_ = String.format("#%06X", 0xFFFFFF and colorId)
            Toast.makeText(this, "color+ $hexColor_", Toast.LENGTH_SHORT).show()
            edit_head.setBackgroundColor(Color.parseColor(hexColor_))


        }
        yellow_.setOnClickListener {
            bg_Color = yellow_.getBackground() as ColorDrawable
            colorId = bg_Color.getColor()
            Toast.makeText(this, "color+ $colorId", Toast.LENGTH_SHORT).show()
            hexColor_ = String.format("#%06X", 0xFFFFFF and colorId)
            Toast.makeText(this, "color+ $hexColor_", Toast.LENGTH_SHORT).show()
            edit_head.setBackgroundColor(Color.parseColor(hexColor_))





        }
        white_.setOnClickListener {
            //  bg_set= white_.getBackground()
            bg_Color = white_.getBackground() as ColorDrawable
            colorId = bg_Color.getColor()
            Toast.makeText(this, "color+ $colorId", Toast.LENGTH_SHORT).show()
            hexColor_ = String.format("#%06X", 0xFFFFFF and colorId)
            Toast.makeText(this, "color+ $hexColor_", Toast.LENGTH_SHORT).show()
            edit_head.setBackgroundColor(Color.parseColor(hexColor_))





        }
        grey_.setOnClickListener {
            bg_Color = grey_.getBackground() as ColorDrawable
            colorId = bg_Color.getColor()
            Toast.makeText(this, "color+ $colorId", Toast.LENGTH_SHORT).show()
            hexColor_ = String.format("#%06X", 0xFFFFFF and colorId)
            Toast.makeText(this, "color+ $hexColor_", Toast.LENGTH_SHORT).show()
            edit_head.setBackgroundColor(Color.parseColor(hexColor_))

        }

        pink_.setOnClickListener {
            bg_Color = pink_.getBackground() as ColorDrawable
            colorId = bg_Color.getColor()
            Toast.makeText(this, "color+ $colorId", Toast.LENGTH_SHORT).show()
            hexColor_ = String.format("#%06X", 0xFFFFFF and colorId)
            Toast.makeText(this, "color+ $hexColor_", Toast.LENGTH_SHORT).show()
            edit_head.setBackgroundColor(Color.parseColor(hexColor_))

        }

        green_.setOnClickListener {
            bg_Color = green_.getBackground() as ColorDrawable
            colorId = bg_Color.getColor()
            Toast.makeText(this, "color+ $colorId", Toast.LENGTH_SHORT).show()
            hexColor_ = String.format("#%06X", 0xFFFFFF and colorId)
            Toast.makeText(this, "color+ $hexColor_", Toast.LENGTH_SHORT).show()
            edit_head.setBackgroundColor(Color.parseColor(hexColor_))

        }


        //-----------------------------------------text color

        red__txt.setOnClickListener {

            bg_color_txt = red__txt.getBackground() as ColorDrawable
            colorId_txt = bg_color_txt.getColor()
            Toast.makeText(this, "color+ $colorId_txt", Toast.LENGTH_SHORT).show()
            hexColor_txt = String.format("#%06X", 0xFFFFFF and colorId_txt)
            Toast.makeText(this, "color+ $hexColor_txt", Toast.LENGTH_SHORT).show()
            edit_head.setTextColor(Color.parseColor(hexColor_txt))

        }

        blue_txt.setOnClickListener {
            bg_color_txt = blue_txt.getBackground() as ColorDrawable
            colorId_txt = bg_color_txt.getColor()
            Toast.makeText(this, "color+ $colorId_txt", Toast.LENGTH_SHORT).show()
            hexColor_txt = String.format("#%06X", 0xFFFFFF and colorId_txt)
            Toast.makeText(this, "color+ $hexColor_txt", Toast.LENGTH_SHORT).show()
            edit_head.setTextColor(Color.parseColor(hexColor_txt))




        }
        black_txt.setOnClickListener {
            bg_color_txt = black_txt.getBackground() as ColorDrawable
            colorId_txt = bg_color_txt.getColor()
            Toast.makeText(this, "color+ $colorId_txt", Toast.LENGTH_SHORT).show()
            hexColor_txt = String.format("#%06X", 0xFFFFFF and colorId_txt)
            Toast.makeText(this, "color+ $hexColor_txt", Toast.LENGTH_SHORT).show()
            edit_head.setTextColor(Color.parseColor(hexColor_txt))


        }
        yellow_txt_.setOnClickListener {
            bg_color_txt = yellow_txt_.getBackground() as ColorDrawable
            colorId_txt = bg_color_txt.getColor()
            Toast.makeText(this, "color+ $colorId_txt", Toast.LENGTH_SHORT).show()
            hexColor_txt = String.format("#%06X", 0xFFFFFF and colorId_txt)
            Toast.makeText(this, "color+ $hexColor_txt", Toast.LENGTH_SHORT).show()
            edit_head.setTextColor(Color.parseColor(hexColor_txt))





        }
        white_txt.setOnClickListener {
            //  bg_set= white_.getBackground()
            bg_color_txt = white_txt.getBackground() as ColorDrawable
            colorId_txt = bg_color_txt.getColor()
            Toast.makeText(this, "color+ $colorId_txt", Toast.LENGTH_SHORT).show()
            hexColor_txt = String.format("#%06X", 0xFFFFFF and colorId_txt)
            Toast.makeText(this, "color+ $hexColor_txt", Toast.LENGTH_SHORT).show()
            edit_head.setTextColor(Color.parseColor(hexColor_txt))





        }
        grey_txt.setOnClickListener {
            bg_color_txt = grey_txt.getBackground() as ColorDrawable
            colorId_txt = bg_color_txt.getColor()
            Toast.makeText(this, "color+ $colorId_txt", Toast.LENGTH_SHORT).show()
            hexColor_txt = String.format("#%06X", 0xFFFFFF and colorId_txt)
            Toast.makeText(this, "color+ $hexColor_txt", Toast.LENGTH_SHORT).show()
            edit_head.setTextColor(Color.parseColor(hexColor_txt))


        }

        pink_txt.setOnClickListener {
            bg_color_txt = pink_txt.getBackground() as ColorDrawable
            colorId_txt = bg_color_txt.getColor()
            Toast.makeText(this, "color+ $colorId_txt", Toast.LENGTH_SHORT).show()
            hexColor_txt = String.format("#%06X", 0xFFFFFF and colorId_txt)
            Toast.makeText(this, "color+ $hexColor_txt", Toast.LENGTH_SHORT).show()
            edit_head.setTextColor(Color.parseColor(hexColor_txt))


        }

        green_txt.setOnClickListener {
            bg_color_txt = green_txt.getBackground() as ColorDrawable
            colorId_txt = bg_color_txt.getColor()
            Toast.makeText(this, "color+ $colorId_txt", Toast.LENGTH_SHORT).show()
            hexColor_txt = String.format("#%06X", 0xFFFFFF and colorId_txt)
            Toast.makeText(this, "color+ $hexColor_txt", Toast.LENGTH_SHORT).show()
            edit_head.setTextColor(Color.parseColor(hexColor_txt))


        }


        /* first_logo.setOnClickListener {
              resID = resources.getIdentifier(
                 "news_logo1", "drawable",
                 packageName
             )

             Toast.makeText(this, "src+ $resID", Toast.LENGTH_SHORT).show()

         }

         second_logo.setOnClickListener {
              resID = resources.getIdentifier(
                 "news_logo2", "drawable",
                 packageName
             )

             Toast.makeText(this, "src+ $resID", Toast.LENGTH_SHORT).show()

         }*/




        ok.setOnClickListener {
            var edit_bg_color: ColorDrawable = edit_head.getBackground() as ColorDrawable
            var edit_bg_id :Int = edit_bg_color.getColor()
            var edit_hex_color:String= String.format("#%06X", 0xFFFFFF and edit_bg_id)
            sendData(edit_hex_color, edit_head.currentTextColor)

        }

        var intent : Intent = getIntent()

        var edittext:String = intent.getStringExtra(MainActivity.GET_TEXT)
        Toast.makeText(this,"text$edittext", Toast.LENGTH_SHORT)
        Log.d(tag,"editText  $edittext")

        var editTextBg:String = intent.getStringExtra(MainActivity.GET_BG)
        Log.d(tag,"editTextBg  $editTextBg")


        var editTextColor:Int = intent.getIntExtra("get_txt_col",0)
        Log.d(tag,"editTextColor  $editTextColor")

        edit_head.setTextColor(editTextColor)
        edit_head.setText(edittext)
        edit_head.setBackgroundColor(Color.parseColor(editTextBg))








    }



    private  fun sendData(hexColor:String,hexColor_txt:Int){

        var  name_txt:String = edit_head.getText().toString()


        var result : Intent = Intent()
        result.putExtra(NAME_,name_txt)
        result.putExtra(COLOR_,hexColor)
        result.putExtra(COLOR_TXT, hexColor_txt)

        setResult(Activity.RESULT_OK,result)
        finish()
    }



    override fun onBackPressed() {

        super.onBackPressed()



    }
}