package kontestbuddybyamitmaity.example.kontestbuddy.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kontestbuddybyamitmaity.example.kontestbuddy.R

class SettingsFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        // Get the version name
        val versionTextView: TextView = view.findViewById(R.id.app_version_text)
        try {
            val packageInfo = requireContext().packageManager.getPackageInfo(
                requireContext().packageName,
                0
            )
            val versionName = packageInfo.versionName
            versionTextView.text = "Version $versionName"
        } catch (e: Exception) {
            versionTextView.text = "Version Unknown"
        }

        return view
    }
}
