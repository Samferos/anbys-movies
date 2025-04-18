package iut.s4.sae.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import iut.s4.sae.ui.viewmodel.TrendingMoviesViewModel

class MainViewFragmentFactory(private val viewmodel: TrendingMoviesViewModel) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            TrendingMoviesFragment::class.java -> TrendingMoviesFragment(viewmodel)
            FavoriteMoviesFragment::class.java -> FavoriteMoviesFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}
