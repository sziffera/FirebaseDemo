package com.example.szian17_hw03_firebase

import android.R.drawable.ic_delete
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


open class UserActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mDatabase: DatabaseReference
    private lateinit var adapter: FirebaseRecyclerAdapter<User, UserViewHolder>
    private lateinit var icon: Drawable
    private lateinit var background: ColorDrawable


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)

        recyclerView = findViewById(R.id.rvUser)
        recyclerView.layoutManager = LinearLayoutManager(this)

        icon = getDrawable(ic_delete)!!

        background = ColorDrawable(Color.parseColor("#4DFF0000"))

        mDatabase = FirebaseDatabase.getInstance().getReference("users")
        mDatabase.keepSynced(true)
    }

    override fun onStart() {
        super.onStart()

        val options =
            FirebaseRecyclerOptions.Builder<User>()
                .setQuery(mDatabase, User::class.java)
                .build()

        adapter =
            object :
                FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: UserViewHolder,
                    position: Int,
                    model: User
                ) {
                    holder.username.text = model.username
                    holder.email.text = model.email
                    holder.description.text = model.description
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                    val context = parent.context
                    val inflater = LayoutInflater.from(context)
                    val v: View =
                        inflater.inflate(R.layout.item_person, parent, false)
                    return UserViewHolder(v)
                }
            }

        recyclerView.adapter = adapter



        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                return false
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.LEFT)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20

                if (dX < 0) {

                    background.setBounds(
                        itemView.right + dX.toInt() - backgroundCornerOffset,
                        itemView.top, itemView.right, itemView.bottom
                    )
                    background.draw(c)

                    val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                    val iconTop = itemView.top + ((itemView.height - icon.intrinsicHeight) / 2)
                    val iconBottom = iconTop + icon.intrinsicHeight

                    val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                    val iconRight = itemView.right - iconMargin

                    Log.i("DRAW", (iconLeft - iconMargin).toString())

                    if (dX < -150) {

                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        icon.draw(c)
                    }


                } else {
                    background.setBounds(0, 0, 0, 0)
                }


                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {

                val pos = viewHolder.adapterPosition
                adapter.getRef(pos).removeValue()

            }
        }).attachToRecyclerView(recyclerView)

        adapter.startListening()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val username: TextView = itemView.findViewById(R.id.usernameTextView)
        val email: TextView = itemView.findViewById(R.id.emailTextView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
    }

}