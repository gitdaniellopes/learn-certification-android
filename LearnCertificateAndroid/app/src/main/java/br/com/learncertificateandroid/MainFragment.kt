package br.com.learncertificateandroid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import br.com.learncertificateandroid.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
    }
}