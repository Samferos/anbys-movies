package iut.s4.sae.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import iut.s4.sae.R
import iut.s4.sae.model.MovieSortType
import iut.s4.sae.ui.viewmodel.SearchViewModel

class SearchFilterBottomSheetFragment() : BottomSheetDialogFragment() {

    private var searchViewModel : SearchViewModel? = null
    private var onFilterConfirm : () -> Unit = {}

    constructor(searchViewModel: SearchViewModel, onFilterConfirm : () -> Unit) : this() {
        this.searchViewModel = searchViewModel
        this.onFilterConfirm = onFilterConfirm
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_filter_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (searchViewModel == null) return

        val ascendingButton = view.findViewById<Button>(R.id.search_filter_button_asc)
        val descendingButton = view.findViewById<Button>(R.id.search_filter_button_desc)

        val radioGroup = view.findViewById<RadioGroup>(R.id.search_filter_sort_types)

        val confirmSort = view.findViewById<Button>(R.id.search_filter_confirm)

        var sortType : MovieSortType = MovieSortType.BY_TITLE
        var ascending : Boolean = false

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.search_filter_by_title -> sortType = MovieSortType.BY_TITLE
                R.id.search_filter_by_rating -> sortType = MovieSortType.BY_RATING
                R.id.search_filter_by_popularity -> sortType = MovieSortType.BY_POPULARITY
            }
        }

        ascendingButton.setOnClickListener {
            ascending = true
        }

        descendingButton.setOnClickListener {
            ascending = false
        }

        confirmSort.setOnClickListener {
            searchViewModel?.sortType = sortType
            searchViewModel?.ascendingSort = ascending
            onFilterConfirm()
        }
    }

    companion object {
        const val TAG = "SearchFilterBottomSheetFragment"
    }
}