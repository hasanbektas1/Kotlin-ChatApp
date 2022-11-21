# Kotlin-ChatApp

![KotlinChatApp1](https://user-images.githubusercontent.com/88456285/203167104-8fe9b614-bafb-4f35-ad5c-a52b446f742d.jpg)
![KotlinChatApp2](https://user-images.githubusercontent.com/88456285/203167263-02fc43d6-7177-4d46-aacd-3e1069781f17.jpg)
![KotlinChatApp3](https://user-images.githubusercontent.com/88456285/203167343-51eaaf00-a7b5-4385-b0b1-8de21ae6a1fb.jpg)

## Kullanilan Teknolojiler

- Firebase
- Fragment
- Navigation
- RecyclerView
- OptionsMenu


 İlk olarak https://firebase.google.com/ sitesine girip Firebase platformuna Gmail ile ücretsiz kayıt oluyoruz.
 Authentication ile kullanıcı giriş işlemleri, Firestore database ile veri kayıt edip sonra uygulamamızda göstermek gibi işlemlerimizi yapabilmek için.
 Uygulamamıza firebase entegrasyon işlemlerimizi tamamladıktan yeni fragmentler yaratıp sonra layout görünümlerini oluşturuyoruz.
 Oluşturduğumuz fragmentleri **navigation** ile kullanmak için gerekli iplementationlarını ekliyoruz.
 Daha sonra fragmentlarımızı navigationa ekleyip hangi ekrandan hangi ekrana gidilecek ise onları aksiyon olarak ekliyoruz.
 Kod kısmına geçicek olursak öncelikle   ``` private lateinit var auth :FirebaseAuth ``` diyip öncelikle bi obje oluşturuyoruz sonra **onCreate** fonksiyonumuz içerisinde initialize edip kullanıma başlar hale getiriyoruz
 
 Fragmentlerde genel olarak kodlarımızı **onViewCreated** fonksiyonu altında yani görünümler oluşturulduktan sonra yazıyoruz ki programımızda aksaklıklar olmasın.
 
  ```
  val currentuser = auth.currentUser

        if (currentuser != null){

            val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
            Navigation.findNavController(view).navigate(action)
        }
   ```
   Daha önce giriş yapılmış ise direkt olarak yazışma sayfasına geçiyoruz, giriş yapılmamış ise giriş sayfasında kullanıcı giriş veya kullanıcı oluşturma adımlarını yazıyoruz.
   
   ```
       binding.loginButton.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val passaword = binding.passawordEditText.text.toString()

            if (email.isNotEmpty() && passaword.isNotEmpty()){

                auth.signInWithEmailAndPassword(email,passaword).addOnSuccessListener { result ->

                    val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                    Navigation.findNavController(it).navigate(action)

                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }else{

                Toast.makeText(requireContext(),"Email ve Passaword giriniz",Toast.LENGTH_LONG).show()
            }
        }
   ```
   
   Yukarıda görüldüğü ``` signInWithEmailAndPassword ``` ile daha önce kayıt olunmuş bir kullanıcı bilgileri ile giriş yapılmak için.
   
   ```
    binding.signButton.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val passaword = binding.passawordEditText.text.toString()

            if(email.isNotEmpty() && passaword.isNotEmpty()){

                auth.createUserWithEmailAndPassword(email,passaword).addOnSuccessListener { result ->

                    val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                    Navigation.findNavController(it).navigate(action)

                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(requireActivity(),"Email ve passaword giriniz",Toast.LENGTH_LONG).show()
            }
        }
   ```
   Burada ise ``` createUserWithEmailAndPassword ``` kodumuzdan anlaşılacagı üzere yeni bir kullanıcı kayıt oluşturmak için.
   
Giriş ekranını geçtikten sonra yazışma ekranında güncel kullanıcı **Send** butonuna bastıgında verileri firebase databaseye yazıyoruz.

 ```
    binding.sendButton.setOnClickListener {

            if(auth.currentUser != null){

                val messagePost = hashMapOf<String , Any>()

                messagePost.put("userEmail",auth.currentUser!!.email!!)
                messagePost.put("messages",binding.Message.text.toString())
                messagePost.put("date",com.google.firebase.Timestamp.now())

                firestore.collection("MessagePost").add(messagePost).addOnSuccessListener {

                    binding.Message.text.clear()

                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }
 ```
 
 Verileri yazdıktan sonra recyclerView ekranında göstermek üzere verilerimizi okuyoruz.
 Bunun için **loadData()** fonksiyonumuzu yazıyoruz
 ```
  private fun loadData(){

        firestore.collection("MessagePost").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()

            }else{
                if (value != null){
                    if (!value.isEmpty){

                        val documents = value.documents

                        messageArrayList.clear()

                        for (document in documents){

                            val email = document.get("userEmail") as String
                            val message = document.get("messages") as String

                            val imessage = ChatModel(email,message)
                            messageArrayList.add(imessage)

                        }

                        chatAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    ```
    
    Son olarak yazışma ekranından çıkıp giriş ekranına dönmek için **onOptionsItemSelected** ile çıkış kodlarımızı yazıyoruz
    
    ```
       override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.signout){

            auth.signOut()

            val action = ChatFragmentDirections.actionChatFragmentToLoginFragment()
            view?.let { Navigation.findNavController(it).navigate(action) }
        }
        return super.onOptionsItemSelected(item)
    }
    ```
    
