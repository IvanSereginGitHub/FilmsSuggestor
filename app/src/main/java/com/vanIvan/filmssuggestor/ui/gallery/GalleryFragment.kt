package com.vanIvan.filmssuggestor.ui.gallery

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import com.vanIvan.filmssuggestor.R
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.marginRight
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vanIvan.filmssuggestor.DBHelper
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
        println(inflater)
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        runOnUiThread {

             // add row at the end of table
        }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var db = DBHelper(requireContext(), null)
        println(db.getDataArray())
        // call findViewById() here
        for (db_row in db.getDataArray()) {
            val table = requireView().findViewById<TableLayout>(R.id.table)
            println(table)
            val row = TableRow(requireActivity())
            row.layoutParams =
                TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
                )
            row.setBackgroundColor(Color.parseColor("#80F0F7F7"))
            row.setPadding(5)
            val id = TextView(requireActivity())
            id.text = db_row.id
            var params = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.MATCH_PARENT,
            )
            params.setMargins(0,0,20,0)
            id.layoutParams = params
            id.gravity = 17
            id.textAlignment = TableRow.TEXT_ALIGNMENT_TEXT_START

            var typedValue = TypedValue();
            requireContext().theme.resolveAttribute(androidx.appcompat.R.attr.actionMenuTextColor, typedValue, true);
            val textColor = typedValue.data
            id.setLinkTextColor(textColor)
            id.setTypeface(null, Typeface.BOLD)
            id.gravity = Gravity.CENTER
            row.addView(id)

            val title = TextView(requireActivity())
            title.text = db_row.title
            title.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1f
            )
            title.gravity = 17
            title.textAlignment = TableRow.TEXT_ALIGNMENT_TEXT_START

            title.setLinkTextColor(textColor)
            title.setTypeface(null, Typeface.BOLD)
            row.addView(title)

            val button = Button(activity)
            button.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.MATCH_PARENT,
            )
            button.text = "Visit IMDB"
            button.setLinkTextColor(textColor)
            button.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/${db_row.imdb_id}"))
                startActivity(i)
            }
            row.addView(button)
            table.addView(row);
        }
        //var table = view.findViewById<View>(R.id.table) as TableLayout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun Fragment?.runOnUiThread(action: () -> Unit) {
        this ?: return
        if (!isAdded) return // Fragment not attached to an Activity
        activity?.runOnUiThread(action)
    }
}