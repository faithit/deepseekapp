
import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.deepseek.firstapp.models.User
import com.deepseek.firstapp.navigation.ROUTE_LOGIN
import com.deepseek.firstapp.navigation.ROUTE_REGISTER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(var navController: NavHostController,var context: Context){
    var mAuth = FirebaseAuth.getInstance()
    //register function
    fun signup(fullname: String,email: String,password: String,confirmpassword: String){
        //validation
        if (email.isBlank()||password.isBlank()||confirmpassword.isBlank()){
            Toast.makeText(context,"Email and password  cannot be blank",Toast.LENGTH_LONG).show()
            return
        }else if(password!=confirmpassword){
            Toast.makeText(context,"passwords do not match", Toast.LENGTH_LONG).show()
        }else{
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val userdata= User(fullname,email,password,mAuth.currentUser!!.uid)
                            //save  user in realtime database
                             val regRef= FirebaseDatabase.getInstance().getReference()
                            .child("Users/" + mAuth.currentUser!!.uid)
                            regRef.setValue(userdata).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(context,"User registered succefully",Toast.LENGTH_LONG).show()
                                    navController.navigate(ROUTE_LOGIN)
                                }else{
                                    Toast.makeText(context,"${it.exception!!.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    }else{
                        navController.navigate(ROUTE_REGISTER)
                    }
                }
        }
    }

    //login function
    //logout function

}