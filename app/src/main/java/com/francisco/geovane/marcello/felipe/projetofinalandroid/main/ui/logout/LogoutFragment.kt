package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.ui.logout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.francisco.geovane.marcello.felipe.projetofinalandroid.R
import com.francisco.geovane.marcello.felipe.projetofinalandroid.login.LoginActivity
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LogoutFragment : Fragment() {

    private lateinit var logoutViewModel: LogoutViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth

        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Tem certeza que deseja sair?")
            .setCancelable(false)
            .setPositiveButton("Sim") { dialog, id ->
                auth.signOut()
                val currentUser: FirebaseUser? = auth.currentUser
                Log.i("USER", currentUser.toString())
                if (currentUser == null){
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }

            }
            .setNegativeButton("Cancelar") { dialog, id ->
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()

        logoutViewModel = ViewModelProvider(this).get(LogoutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_logout, container, false)

//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        logoutViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }
}