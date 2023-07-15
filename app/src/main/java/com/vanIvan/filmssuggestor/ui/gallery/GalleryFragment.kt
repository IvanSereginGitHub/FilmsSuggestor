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
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var db = DBHelper(requireContext(), null)

        // call findViewById() here
        for (db_row in db.getDataArray()) {
            val table = requireView().findViewById<TableLayout>(R.id.table)

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