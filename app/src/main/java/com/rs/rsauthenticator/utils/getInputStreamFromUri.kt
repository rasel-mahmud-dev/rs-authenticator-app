import android.content.Context
import android.net.Uri
import java.io.InputStream

fun getInputStreamFromUri(context: Context, uri: Uri): InputStream? {
    return try {
        context.contentResolver.openInputStream(uri)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}