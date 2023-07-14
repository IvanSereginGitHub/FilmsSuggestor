package com.vanIvan.filmssuggestor.ui.gallery

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vanIvan.filmssuggestor.databinding.FragmentArchiveBinding


class GalleryFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
                ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val table: TableLayout = findViewById(R.id.table)
//        for (i in 0..4) {
//            val row = TableRow(this)
//            val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
//            row.layoutParams = lp
//            val textView = TextView(this)
//            textView.text = "Name $i"
//            val editText = EditText(this)
//            row.addView(textView)
//            table.addView(row, i)
//        }
//        val textView: TextView = binding.textGallery
//        galleryViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }
//
//    fun init() {
//        val stk = view?.findViewById<TableLayout>(R.id.table)
//        val tbrow0 = TableRow(this)
//        val tv0 = TextView(this)
//        tv0.text = " Sl.No "
//        tv0.setTextColor(Color.WHITE)
//        tbrow0.addView(tv0)
//        val tv1 = TextView(this)
//        tv1.text = " Product "
//        tv1.setTextColor(Color.WHITE)
//        tbrow0.addView(tv1)
//        val tv2 = TextView(this)
//        tv2.text = " Unit Price "
//        tv2.setTextColor(Color.WHITE)
//        tbrow0.addView(tv2)
//        val tv3 = TextView(this)
//        tv3.text = " Stock Remaining "
//        tv3.setTextColor(Color.WHITE)
//        tbrow0.addView(tv3)
//        stk.addView(tbrow0)
//        for (i in 0..24) {
//            val tbrow = TableRow(this)
//            val t1v = TextView(this)
//            t1v.text = "" + i
//            t1v.setTextColor(Color.WHITE)
//            t1v.gravity = Gravity.CENTER
//            tbrow.addView(t1v)
//            val t2v = TextView(this)
//            t2v.text = "Product $i"
//            t2v.setTextColor(Color.WHITE)
//            t2v.gravity = Gravity.CENTER
//            tbrow.addView(t2v)
//            val t3v = TextView(this)
//            t3v.text = "Rs.$i"
//            t3v.setTextColor(Color.WHITE)
//            t3v.gravity = Gravity.CENTER
//            tbrow.addView(t3v)
//            val t4v = TextView(this)
//            t4v.text = "" + i * 15 / 32 * 10
//            t4v.setTextColor(Color.WHITE)
//            t4v.gravity = Gravity.CENTER
//            tbrow.addView(t4v)
//            stk.addView(tbrow)
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}