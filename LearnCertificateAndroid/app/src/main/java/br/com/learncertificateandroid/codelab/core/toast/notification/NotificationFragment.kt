package br.com.learncertificateandroid.codelab.core.toast.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import br.com.learncertificateandroid.R
import br.com.learncertificateandroid.databinding.FragmentNotificationBinding

private const val NOTIFICATION_ID = 0
private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val ACTION_UPDATE = "ACTION_UPDATE_NOTIFICATION"
private const val ACTION_CANCEL = "ACTION_CANCEL_NOTIFICATION"
private const val ACTION_DELETE_ALL = "ACTION_DELETED_NOTIFICATIONS"

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private lateinit var notificationManager: NotificationManager
    private val notificationReceiver = NotificationReceiver()

    private lateinit var binding: FragmentNotificationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.bind(view)
        setupUiButtonListeners()
        setupUiButtonStates(enableNotify = true, enableUpdate = false, enableCancel = false)
        createNotificationChannel()
        registerNotificationReceiver()
    }

    private fun registerNotificationReceiver() {
       val notificationActionFilters = IntentFilter()
        notificationActionFilters.addAction(ACTION_UPDATE)
        notificationActionFilters.addAction(ACTION_DELETE_ALL)
        notificationActionFilters.addAction(ACTION_CANCEL)
        requireActivity().registerReceiver(notificationReceiver, notificationActionFilters)
    }

    private fun createNotificationChannel() {
        notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // o que vai aparecer nas configurações do aparelho
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID
                "Mascot Notification", NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED

            notificationManager.createNotificationChannel(notificationChannel)
        }else{
            // caso o aparelho for inferior a api 26
        }
    }

    private fun setupUiButtonStates(
        enableNotify: Boolean,
        enableUpdate: Boolean,
        enableCancel: Boolean
    ) {
        binding.notify.isEnabled = enableNotify
        binding.update.isEnabled = enableUpdate
        binding.cancel.isEnabled = enableCancel
    }

    private fun setupUiButtonListeners() = with(binding) {
        notify.setOnClickListener { sendNotification() }
        update.setOnClickListener { updateNotification() }
        cancel.setOnClickListener { cancelNotification() }
    }

    private fun sendNotification() {
       val builder = getNotificationBuilder()
        createNotificationAction(builder, NOTIFICATION_ID, ACTION_UPDATE, "atualiza")
        createNotificationAction(builder, NOTIFICATION_ID, ACTION_CANCEL, "remover")

        val deleteAllAction = Intent(ACTION_DELETE_ALL)
        val deleteAction = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            deleteAllAction,
            PendingIntent.FLAG_ONE_SHOT

        )
        builder.setDeleteIntent(deleteAction)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
        setupUiButtonStates(enableNotify = false, enableUpdate = true, enableCancel = true)
    }

    private fun createNotificationAction(
        builder: NotificationCompat.Builder,
        notificationId: Int,
        actionId: String,
        actionTitle: String
    ) {
        val updateActionFilter = Intent(actionId)
        val updateAction = PendingIntent.getBroadcast(
            requireContext(),
            notificationId,
            updateActionFilter,
            PendingIntent.FLAG_ONE_SHOT
        )
        builder.addAction(
            R.drawable.ic_launcher_foreground,
            actionTitle,
            updateAction
        )
    }

    private fun updateNotification() {
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.ic_favorite)

        val notification = getNotificationBuilder()

        notificationManager.notify(NOTIFICATION_ID, notification.build())
        setupUiButtonStates(enableNotify = false, enableUpdate = false, enableCancel = true)
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(requireContext(), NotificationFragment::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            requireContext(),
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(requireContext(), PRIMARY_CHANNEL_ID)
            .setContentTitle("Voce recebe uma notificação")
            .setContentText("Show")
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(false)
    }

    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
        setupUiButtonStates(enableNotify = true, enableUpdate = false, enableCancel = false)
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(notificationReceiver)
        super.onDestroy()
    }
}