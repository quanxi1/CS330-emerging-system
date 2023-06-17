package com.example.pj4test.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pj4test.ProjectConfiguration
import com.example.pj4test.audioInference.SnapClassifier
import com.example.pj4test.databinding.FragmentAudioBinding
import com.example.pj4test.ItemViewModel

import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer


class AudioFragment: Fragment(), SnapClassifier.DetectorListener {
    private val TAG = "AudioFragment"

    private var _fragmentAudioBinding: FragmentAudioBinding? = null

    private val fragmentAudioBinding
        get() = _fragmentAudioBinding!!

    // classifiers
    lateinit var snapClassifier: SnapClassifier

    // views
    lateinit var snapView: TextView
    private val viewModel: ItemViewModel by activityViewModels()

    private var currentPersonDetected: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentAudioBinding = FragmentAudioBinding.inflate(inflater, container, false)

        return fragmentAudioBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snapView = fragmentAudioBinding.SnapView

        snapClassifier = SnapClassifier()
        snapClassifier.initialize(requireContext())
        snapClassifier.setDetectorListener(this)
    }

    override fun onPause() {
        super.onPause()
        snapClassifier.stopInferencing()
    }

    override fun onResume() {
        super.onResume()
        snapClassifier.startInferencing()
    }

    override fun onResults(score: Float) {
        activity?.runOnUiThread {
            if (viewModel.isPersonDetected == false) {
                if (currentPersonDetected == true) {
                    snapClassifier.setInterval(1200L)
                    currentPersonDetected = false
                }
            }
            else if (viewModel.isPersonDetected == true) {
                if (currentPersonDetected == false) {
                    snapClassifier.setInterval(33L)
                    currentPersonDetected = true
                }
            }
            if (score > SnapClassifier.THRESHOLD) {
                snapView.text = "SOMEBODY IS COMMING"
                snapView.setBackgroundColor(ProjectConfiguration.activeBackgroundColor)
                snapView.setTextColor(ProjectConfiguration.activeTextColor)
                viewModel.footstepDetected(true)

            } else {
                snapView.text = "NOBODY IS AROUND"
                snapView.setBackgroundColor(ProjectConfiguration.idleBackgroundColor)
                snapView.setTextColor(ProjectConfiguration.idleTextColor)
                viewModel.footstepDetected(false)
            }
        }
    }
}