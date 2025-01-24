import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchBarView(
    modifier: Modifier = Modifier,
    placeholderText: String = "Search logs...",
    onSearchQueryChanged: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    BasicTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onSearchQueryChanged(it.text)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colors.surface,
                        MaterialTheme.shapes.small
                    )
                    .background(color = Color.DarkGray)
            ) {
                if (searchQuery.text.isEmpty()) {
                    Text(placeholderText)
                }
                innerTextField()
            }
        }
    )
}