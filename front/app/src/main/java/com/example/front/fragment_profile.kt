package com.example.front

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class fragment_profile : Fragment() {
    private lateinit var profileDisplayLayout: View
    private lateinit var addProfileButton: Button
    private lateinit var testingButton: Button
    private lateinit var apiService: ApiService
    private lateinit var textView_user_email_show: TextView
    private lateinit var textView_address_show: TextView
    private lateinit var textView_user_name_show: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var petAdapter: PetAdapter
    private lateinit var imageView: ImageView
    private var petProfiles: List<PetDTO> = emptyList()
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        apiService = RetrofitClient.getRetrofitInstance(requireContext()).create(ApiService::class.java)

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        recyclerView = view.findViewById(R.id.profile_recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val userId: Int? = arguments?.getInt("userId")

        Log.d("ProfileFragment", "Received userId: $userId")

        petAdapter = PetAdapter(petProfiles) { profile ->
            Log.d("petId?", "petId = ${profile.petId}")
            val intent = Intent(context, ProfileDetailActivity::class.java)
            intent.putExtra("petId", profile.petId)
            startActivity(intent)
        }
        recyclerView.adapter = petAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView_user_name_show = view.findViewById(R.id.textView_user_name_show)
        textView_user_email_show = view.findViewById(R.id.textView_user_email_show)
        textView_address_show = view.findViewById(R.id.textView_address_show)

        recyclerView = view.findViewById(R.id.profile_recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val userId = arguments?.getInt("userId")
        Log.d("ProfileFragment", "userId from arguments: $userId")
        if (userId != null) {
            loadUser(userId)
            loadPet(userId)
        } else {
            showToast("유저를 찾을 수 없습니다!")
        }

        val fab: FloatingActionButton = view.findViewById(R.id.button_to_profile_add)
        fab.setOnClickListener        {
            navigateToProfileAddActivity(id)

//        val buttonToProfileAdd = view.findViewById<Button>(R.id.button_to_profile_add)
//        buttonToProfileAdd?.setOnClickListener {
//            navigateToProfileAddActivity(id)
        }
    }

//        textView_user_name_show= view.findViewById(R.id.textView_user_name_show)
//        textView_user_email_show = view.findViewById(R.id.textView_user_email_show)
//        textView_address_show = view.findViewById(R.id.textView_address_show)


    private fun navigateToProfileAddActivity(userId: Int?) {
        val userId = arguments?.getInt("userId")
        Log.d("userIdBeforeAdd?", "UserId = $userId")
        userId?.let { id ->
            val intent = Intent(requireContext(), ProfileAddActivity::class.java)
            intent.putExtra("userId", id)
            startActivity(intent)
        }
    }

    private fun loadPet(userId: Int) {
        apiService.getPetProfileByUserId(userId).enqueue(object : Callback<List<PetDTO>> {
            override fun onResponse(call: Call<List<PetDTO>>, response: Response<List<PetDTO>>) {
                if (response.isSuccessful) {
                    Log.d("ProfileFragment", "userId from arguments: $userId")
                    Log.d("ProfileFragment", "수신된 프로필: $petProfiles")
                    petProfiles = response.body() ?: emptyList()
                    petAdapter = PetAdapter(petProfiles) { petDTO ->
                        val intent = Intent(context, ProfileDetailActivity::class.java).apply {
                            putExtra("petId", petDTO.petId)
                        }
                        startActivity(intent)
                    }
                    recyclerView.adapter = petAdapter
                } else {
                    Log.e(
                        "ProfileFragment",
                        "Response not successful: ${response.code()} - ${response.message()}"
                    )
                    Log.e("ProfileFragment", "Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<PetDTO>>, t: Throwable) {
                showToast("Network error!")
            }
        })
    }

    private fun loadUser(userId: Int) {
        apiService.getProfileAboutUser(userId).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                Log.d("ProfileFragment", "API Response Code: ${response.code()}")
                Log.d("ProfileFragment", "Response Body: ${response.body()}")
                Log.d("ProfileFragment", "Response Error Body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        textView_user_name_show.text = user.userName
                        textView_user_email_show.text = user.email
                        textView_address_show.text = user.userAddress
                    } ?: run {
                        Log.d("ProfileFragment", "Response body is null")
                    }
                } else {
                    Log.d(
                        "ProfileFragment",
                        "Response not successful: ${response.errorBody()?.string()}"
                    )
                    showToast("요청 실패!")
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                Log.e("ProfileFragment", "API Failure: ${t.message}")
                showToast("네트워크 연결 에러!")
            }
        })
    }

    // Activity에서 결과 받았을 때 호출되는 메서드.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PROFILE_DETAIL && resultCode == Activity.RESULT_OK) {
            val refreshProfileFragment =
                data?.getBooleanExtra("refreshProfileFragment", false) ?: false
            val userId: Int? = arguments?.getInt("userId")
            if (refreshProfileFragment) {
                loadUser(userId!!) // 데이터 새로 고침
                loadPet(userId)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PROFILE_DETAIL = 1
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        val userId = arguments?.getInt("userId")
        Log.d("ProfileFragmentReset", "userId from arguments: $userId")
        if (userId != null) {
            loadUser(userId)
            loadPet(userId)
        }
    }
}

/*


//        val testingButton = view.findViewById<Button>(R.id.testingButton)

//        testingButton.setOnClickListener {
//           testConnection()
//        }
        return view
    }


    private fun testConnection() {

        apiService.testConnection().enqueue(object : Callback<TestResponse> {
            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>){
                if(response.isSuccessful) {
                    Toast.makeText(context,"Server Response: ${response.body()?.message}", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Request failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show();
            }
        })
    }
}














public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
// private static final String BASE_URL = "http://10.0.2.2:8080/"으로 설정한 baseURL 넣고

                    .addConverterFactory(GsonConverterFactory.create())
// 안드로이드 스튜디오에서 받은 값들을 JSON 파일로 변환해서 = X
// GSON 컨버터만 추가했다(Java 객체 <--> JSON 객체) = O

                    .build();
// 서버로 보낸다. = X  , 인스턴스를 생성한다 = O(아직 서버로 안보냄. 보낼 인스턴스를 만들기만 했을 뿐)

        }
        return retrofit;
// 다시 null값으로 초기화
    }
-> 이건 안드로이드 스튜디오에서 회원가입을 위해 받은 정보를 JSON파일로 변환해서 서버로 보내는 역할 = X
-> 이 인스턴스에 신호 담아서 보낼거임(아직 안보냄). 신호 전달만 만듬.


    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
//  RetrofitClient라는 클래스 안에 getRetrofitInstance()를 만들어라(ApiService 안의 class로)
-> 신호에 @POST, user 객체 생성, Call<UserResponse>를 넣은 것? = X
-> RetrofitClient.getRetrofitInstance() 메소드로 인스턴스 얻고, 이걸로 ApiService 구현체 생성.



        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
// user라는 객체 안에 class User에 매개변수 선언한 name, password 넣음



        Call<UserResponse> call = apiService.createUser(user);
// UserResponse 객체 call 생성, user에 들어간 name과 password로 apiService의 createUser 실행 = 대충 비슷?
-> ApiService의 createUser 메소드 호출, User객체를 서버로 전송할 Call 객체 생성.
이 Call 객체가 안.스 <-> STS로 요청 보내고 응답 처리하는데 사용.


        call.enqueue(new Callback<UserResponse>() {
// 생성된 call의 데이터를 새 UserResponse 생성해서 그 안에 데이터 담고 callback(STS에서 안드로이드로 신호 전달)
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
// 서버의 응답을 비동기적으로 처리하는 콜백 메소드

                if (response.isSuccessful()) {
//	response가 성공했을 때, 즉 name과 password를 제대로 담았을 때. = X
-> 서버의 응답이 성공적일 때

                    UserResponse userResponse = response.body();
//	STS 안에 name과 password를 보낸 결과를 userResponse에 담아라. = X
-> 서버로부터 받은 응답 본문을 UserResponse 객체로 변환함.

                    if (userResponse != null && userResponse.isSuccess()) {
//	만약 userResponse가 널이 아니다 = 신호는 제대로 갔다(STS에 name과 password를 넣었든 넣지못했든)
//	userResponse가 성공했다 = STS에 name과 password를 제대로 넣었다.
-> 신호도 제대로 갔고 db에 data도 제대로 넣었다. = XXXXX
->




     회원가입 성공 처리 // 회원가입 했을 때 안드로이드 스튜디오에서 어떻게 할건지(화면을 전달할건지, 같은)
                    } else {
//  if (userResponse != null && userResponse.isSuccess()를 실패했을 때,
// 신호를 제대로 못 보냈거나, db 조건이랑 다를 때 처리. XXXXX
-> 서버와의 응답을 실패했을 때. 인터넷 연결 문제라거나, 서버 다운이라거나, 요청 자체의 오류거나 하는
                    }
                }
            }

위까지는 신호는 제대로 발생했고, 신호가 서버에 갔냐 안갔냐, 갔는데 올바른 응답이 왔냐 안왔냐의 문제



아래는 신호가 제대로 발생하지 않은 문제. or



@Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                //  if (response.isSuccessful()) 실패 처리, name이나 password를 제대로 못 담았을 때(name이 2글자 이상인데 1글자만 했다거나)
-> XXXXXXX
-> 이거는 걍 네트워크 오류일 때


            }
        });
    }
}





public class MainActivity extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // 회원가입 요청
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("testPass");
        user.setEmail("test@Test.com");
        user.setUserPhone("010-0000-0000");
        user.setUserAddress("부산에 비옴");

        Call<UserResponse> call = apiService.createUser(user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null && userResponse.isSuccess()) {
                        Toast.makeText(this, "회원 가입을 축하드립니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "회원 가입 시 오류가 있습니다 다시 시도해 주세요!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(this, "네트워크 연결 실패입니다! 인터넷이 연결된 환경에서 다시 시도해주세요!", Toast.LENGTH_SHORT).show()
            }
        });
    }
}






*/














/*

// ProfileAddActivity로 전환하는 Intent 생성
    val intent = Intent(requireContext(), ProfileAddActivity::class.java)
    startActivity(intent)


override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    val view = inflater.inflate(R.layout.fragment_profile, container, false)
    val buttonToProfileAdd = view.findViewById<Button>(R.id.button_to_profile_add)
    buttonToProfileAdd.setOnClickListener {
        (activity as? MainActivity)?.navigationToProfileAddActivity()
    }
    loadProfileData()
    return view
}

//    private fun loadProfileData() {
//        firestore.collection("profiles").document("your_user_id_here").get()
//            .addOnSuccessListener { document ->
//                if(document.exists()) {
//                    val profileData = document.toObject(ProfileAddActivity.ProfileData::class.java)
//
//                    // 프로필 데이터가 있으면 프로필 정보 표시
//                    profileDisplayLayout.visibility = View.VISIBLE
//                    addProfileButton.visibility = View.GONE
//
//                    val imageView: ImageView = view?.findViewById(R.id.profile_add_button)!!
//                    val nameTextView: TextView = view?.findViewById(R.id.profile_add_name)!!
//
//                    nameTextView.text = profileData?.name
//
//                    if(profileData?.imageUrl != null) {
////                        Picasso.get().load(profileData.imageUrl).into(imageView)
//                    }
//                } else {
//                    profileDisplayLayout.visibility = View.GONE
//                    addProfileButton.visibility = View.VISIBLE
//                }
//            }
//            .addOnFailureListener {
////                Toast.makeText(this, "등록 실패했습니다!", Toast.LENGTH_SHORT).show()
//            }
//    }














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