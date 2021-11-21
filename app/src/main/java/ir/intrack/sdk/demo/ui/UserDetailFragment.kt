package ir.intrack.sdk.demo.ui

import java.text.SimpleDateFormat
import java.util.*
import org.json.JSONObject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.Toast

import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.switchmaterial.SwitchMaterial

import ir.intrack.sdk.demo.R
import ir.intrack.sdk.demo.toMap

//InTrack
import ir.intrack.android.sdk.InTrack
import ir.intrack.android.sdk.UserDetails
import ir.intrack.android.sdk.ApiUserGender


class UserDetailFragment : Fragment() {

    @Throws(IllegalArgumentException::class)
    fun getInfo(): UserDetails {
        val details = UserDetails()
        val userGender = mapOf(
            null to null,
            "male" to ApiUserGender.MALE,
            "female" to ApiUserGender.FEMALE,
            "other" to ApiUserGender.OTHER
        )
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        details.userId = this.view?.findViewById<TextInputLayout>(R.id.userId)?.editText?.text.toString()
        details.firstName = this.view?.findViewById<TextInputLayout>(R.id.userFirstName)?.editText?.text.toString()
        details.lastName = this.view?.findViewById<TextInputLayout>(R.id.userLastName)?.editText?.text.toString()
        details.email = this.view?.findViewById<TextInputLayout>(R.id.userEmail)?.editText?.text.toString()
        details.emailOptIn = this.view?.findViewById<SwitchMaterial>(R.id.userEmailOptIn)?.isChecked
        details.hashedEmail = this.view?.findViewById<TextInputLayout>(R.id.userHashedEmail)?.editText?.text.toString()
        details.phone = this.view?.findViewById<TextInputLayout>(R.id.userPhone)?.editText?.text.toString()
        details.smsOptIn = this.view?.findViewById<SwitchMaterial>(R.id.userSmsOptIn)?.isChecked
        details.hashedPhone = this.view?.findViewById<TextInputLayout>(R.id.userHashedPhone)?.editText?.text.toString()
        details.gender = userGender[this.view?.findViewById<RadioButton>(view?.findViewById<RadioGroup>(R.id.userGender)!!.checkedRadioButtonId)?.text]
        details.company = this.view?.findViewById<TextInputLayout>(R.id.userCompany)?.editText?.text.toString()
        details.country = this.view?.findViewById<TextInputLayout>(R.id.userCountry)?.editText?.text.toString()
        details.city = this.view?.findViewById<TextInputLayout>(R.id.userCity)?.editText?.text.toString()
        details.state = this.view?.findViewById<TextInputLayout>(R.id.userState)?.editText?.text.toString()
        val birthDay= this.view?.findViewById<TextInputLayout>(R.id.userBirthDay)?.editText?.text.toString()
        if(birthDay!=""){
            details.birthday = dateFormatter.parse(birthDay)
        }
        details.userAttributes = toMap(JSONObject(this.view?.findViewById<TextInputLayout>(R.id.userAttributes)?.editText?.text.toString()))

        return details
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_details, container, false)

        val loginBtn = view.findViewById<Button>(R.id.userLoginBtn)
        val updateBtn = view.findViewById<Button>(R.id.userUpdateBtn)
        val logoutBtn = view.findViewById<Button>(R.id.userLogoutBtn)
        val userIdField = view.findViewById<TextInputLayout>(R.id.userId)

        val userId = InTrack.getUserId()
        if(userId != null){
            userIdField?.editText?.setText(userId)
            userIdField?.isEnabled = false
            loginBtn?.isEnabled = false
        }else{
            logoutBtn.isEnabled = false
            updateBtn.isEnabled = false
        }


        loginBtn.setOnClickListener {
            try {
                val details = this.getInfo()
                InTrack.recordLogin(details)

                loginBtn.isEnabled = false
                userIdField.isEnabled = false
                logoutBtn.isEnabled = true
                updateBtn.isEnabled = true

                val toast = Toast.makeText(view.context, "login recorded!", Toast.LENGTH_SHORT)
                toast.show()
            } catch (t: Throwable){
                val toast = Toast.makeText(view.context, "input is no valid!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        updateBtn.setOnClickListener {
            try {
                val details = this.getInfo()
                InTrack.updateProfile(details)
                val toast = Toast.makeText(view.context, "profile updated", Toast.LENGTH_SHORT)
                toast.show()
            } catch (t: Throwable){
                val toast = Toast.makeText(view.context, "input is no valid!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        logoutBtn.setOnClickListener {
            InTrack.recordLogout()

            loginBtn.isEnabled = true
            userIdField.isEnabled = true
            logoutBtn.isEnabled = false
            updateBtn.isEnabled = false

            val toast = Toast.makeText(view.context, "user logged out!", Toast.LENGTH_SHORT)
            toast.show()
        }

        return view
    }

}