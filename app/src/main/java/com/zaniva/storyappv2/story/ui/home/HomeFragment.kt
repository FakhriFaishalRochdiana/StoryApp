package com.zaniva.storyappv2.story.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaniva.storyappv2.R
import com.zaniva.storyappv2.connection.SessionManager
import com.zaniva.storyappv2.databinding.FragmentHomeBinding
import com.zaniva.storyappv2.databinding.RvItemBinding
import com.zaniva.storyappv2.detail.DetailActivity
import com.zaniva.storyappv2.login.LoginActivity
import com.zaniva.storyappv2.story.Stories
import com.zaniva.storyappv2.upload.UploadActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.core.util.Pair

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: HomeViewModel
    private lateinit var adapter: Adapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        _binding    = FragmentHomeBinding.bind(view)
        adapter     = Adapter()
        adapter.notifyDataSetChanged()

        adapter.setOnClick(object: Adapter.OnClick{
            override fun onClicked(data: Stories, card: RvItemBinding) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.NAME, data.name)
                intent.putExtra(DetailActivity.PHOTO, data.photo)
                intent.putExtra(DetailActivity.DESC, data.description)
                intent.putExtra(DetailActivity.DATE, data.date)

                val OpCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        view.context as Activity,
                        Pair(card.imgStory, "photo"),
                        Pair(card.tvName, "name"),
                        Pair(card.tvDesc, "desc"),
                        Pair(card.tvDate, "date")
                    )
                view.context.startActivity(intent, OpCompat.toBundle())

//                val test = data.name
//                val test2 = data.date
//
//                Intent(requireContext(), DetailActivity::class.java).also {
//                    it.putExtra(DetailActivity.NAME, data.name)
//                    it.putExtra(DetailActivity.PHOTO, data.photo)
//                    it.putExtra(DetailActivity.DESC, data.description)
//                    it.putExtra(DetailActivity.DATE, data.date)
//                    startActivity(it)
//                    val optionsCompat: ActivityOptionsCompat =
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            view.context as Activity,
//                            Pair(card.imgStory, data.photo)
//                        )
//                }
            }
        })

        val pref = SessionManager.get(requireContext().dataStore)
        vm = ViewModelProvider(this, HomeVMFactory(pref)).get(
            HomeViewModel::class.java
        )

        binding.apply {
            rvStories.setHasFixedSize(true)
            rvStories.layoutManager = LinearLayoutManager(activity)
            rvStories.adapter = adapter
        }

        showLoading(true)
        var token = "empty"
        vm.getToken().observe(viewLifecycleOwner){
            token = it
            vm.setStories(token)
            vm.getStories().observe(viewLifecycleOwner){
                if (it != null){
                    adapter.setList(it)
                    showLoading(false)
                }
            }

            binding.fabAdd.setOnClickListener {
                Intent(requireContext(), UploadActivity::class.java).also {
                    it.putExtra(UploadActivity.TOKEN, token)
                    startActivity(it)
                }
            }
        }




    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.logout -> {
                GlobalScope.launch {
                    logout()
                }
                activity?.let {
                    Intent(requireContext(), LoginActivity::class.java).also {
                        startActivity(it)
                    }
                    it.finish()

                }
                Toast.makeText(requireContext(), "Berhasil Logout!", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    suspend fun logout(){
        val pref = SessionManager.get(requireContext().dataStore)
        pref.logout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.pbHome.visibility = View.VISIBLE
        } else {
            binding.pbHome.visibility = View.GONE
        }
    }

}