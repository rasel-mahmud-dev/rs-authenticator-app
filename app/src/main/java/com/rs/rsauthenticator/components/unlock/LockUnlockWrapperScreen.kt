import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.rs.rsauthenticator.components.unlock.UnlockScreen
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.layout.MainApp
import com.rs.rsauthenticator.state.AppState

@Composable
fun LockUnlockWrapperScreen(appStateDbHelper: AppStateDbHelper) {
    val storedPin by remember { mutableStateOf(appStateDbHelper.getPin()) }

    val context = LocalContext.current

    AnimatedVisibility(
        visible = AppState.isLocked,
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
                AppState.updateLockState(false)
            },
            storedPin = storedPin ?: ""
        )
    }

    AnimatedVisibility(
        visible = !AppState.isLocked,
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
