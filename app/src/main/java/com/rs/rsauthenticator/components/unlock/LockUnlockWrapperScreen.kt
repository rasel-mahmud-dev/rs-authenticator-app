import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.rs.rsauthenticator.components.unlock.UnlockScreen
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.layout.MainApp

@Composable
fun LockUnlockWrapperScreen(appStateDbHelper: AppStateDbHelper, onUnlock: () -> Unit) {
    var isUnlocked by remember { mutableStateOf(false) }
    val storedPin by remember { mutableStateOf(appStateDbHelper.getPin()) }
    val isPinEnabled by remember { mutableStateOf(appStateDbHelper.isPinEnabled()) }

    if (isPinEnabled && storedPin.isNullOrEmpty()) {
        isUnlocked = true
    }

    AnimatedVisibility(
        visible = !isUnlocked,
        enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(
            initialOffsetX = { it / 5 },
            animationSpec = tween(300)
        ),
        exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
            targetOffsetX = { -it / 5 },
            animationSpec = tween(300)
        )
    ) {
        UnlockScreen(
            onUnlock = {
                isUnlocked = true
            },
            storedPin = storedPin ?: ""
        )
    }

    AnimatedVisibility(
        visible = isUnlocked,
        enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(
            initialOffsetX = { -it / 5 },
            animationSpec = tween(300)
        ),
        exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
            targetOffsetX = { it / 5 },
            animationSpec = tween(300)
        )
    ) {
        MainApp()
    }
}
