package kontestbuddybyamitmaity.example.kontestbuddy.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kontestbuddybyamitmaity.example.kontestbuddy.R


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View  = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialization
        initialization()

        //Retrieve Data From Local Storage and Set Data into The Container

        // Update the Refresh Button
        // Retrieve the details from firebase -> Call the API to Fetch Latest Data -> set into Container -> Store Local Storage



        return view
    }

    private fun initialization() {
        TODO("Not yet implemented")
    }

}