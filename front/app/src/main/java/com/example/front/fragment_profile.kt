package com.example.front

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.Nullable
import androidx.fragment.app.replace

class fragment_profile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
    val buttonToProfileAdd = view.findViewById<Button>(R.id.button_to_profile_add)
    buttonToProfileAdd.setOnClickListener {
        // ProfileAddActivity로 전환하는 Intent 생성
        val intent = Intent(requireContext(), ProfileAddActivity::class.java)
        startActivity(intent)
    }

    return view
}
}













    /*


    private var buttonToProfileAdd: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        buttonToProfileAdd = view.findViewById(R.id.button_to_profile_add)
        buttonToProfileAdd?.setOnClickListener {
            buttonToProfileAdd?.isEnabled = false
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_profile_container, fragment_profile_add())
                .addToBackStack(null)
                .commit()
        }

        // 결과 수신 설정
        parentFragmentManager.setFragmentResultListener(PROFILE_ADD_RESULT_KEY, this) { key, bundle ->
            if (key == PROFILE_ADD_RESULT_KEY) {
                val buttonEnabled = bundle.getBoolean(BUTTON_ENABLED_RESULT, false)
                if (buttonEnabled) {
                    buttonToProfileAdd?.isEnabled = true
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        buttonToProfileAdd?.isEnabled = true
    }
}


    private var buttonToProfileAdd: Button? = null
    private var isButtonEnabled: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        buttonToProfileAdd = view.findViewById(R.id.button_to_profile_add)
        buttonToProfileAdd?.setOnClickListener {
            Log.d("FragmentProfile", "Button clicked, navigating to profile add")
            buttonToProfileAdd?.isEnabled = false
            isButtonEnabled = false
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_profile_container, fragment_profile_add())
                .addToBackStack(null)
                .commit()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FragmentProfile", "onViewCreated called")
        buttonToProfileAdd = view.findViewById(R.id.button_to_profile_add)
        buttonToProfileAdd?.isEnabled = isButtonEnabled
    }

    override fun onResume() {
        super.onResume()
        Log.d("FragmentProfile", "onResume called")
        buttonToProfileAdd?.isEnabled = isButtonEnabled
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("button_state", isButtonEnabled)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            isButtonEnabled = savedInstanceState.getBoolean("button_state", true)
        }
    }
}


    private var buttonToProfileAdd: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // button_to_profile_add 버튼 클릭하면 fragment_profile_add로 전환 이벤트 생성
        buttonToProfileAdd = view.findViewById(R.id.button_to_profile_add)
        buttonToProfileAdd?.setOnClickListener {
            // fragment_profile -> fragment_profile_add로 화면 전환.

            // 버튼을 비활성화
            buttonToProfileAdd?.isEnabled = false
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_profile_container, fragment_profile_add())
                //반려동물 추가하기 페이지 활성화(화면 전환)
                //if문 이용해서 뒤로가기 활성화
                .addToBackStack(null)
//              뒤로가기버튼(있다면) 누르면 profile로.
                .commit()
//              //(화면 전환에 대한 완료 및 반영)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonToProfileAdd = view.findViewById(R.id.button_to_profile_add)
        buttonToProfileAdd?.isEnabled = true
        // onViewCreated에서 추가적인 초기화 작업 수행 가능
        // 예를 들어, 버튼의 기타 설정 등
    }

    override fun onResume() {
        super.onResume()
        // 화면이 다시 보일 때마다 버튼을 활성화
        buttonToProfileAdd?.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        buttonToProfileAdd?.isEnabled = false
    }
}

/*
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if(keyCode === KeyEvent.KEYCODE_BACK) {
        }
        // 버튼을 비활성화하여 클릭 이벤트를 막음
        buttonToProfileAdd.setClickable(false);
    }
*/
//    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_profile.
         */
        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            fragment_profile().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//

     */