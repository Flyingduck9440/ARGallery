package com.arsoft.argalleryview.module.preview.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arsoft.argalleryview.module.main.bottomSheetHeight

@Composable
fun PreviewBottomSheet(
    selectedItemsCount: Int,
    id: String,
    isSelected: Boolean,
    onCheckedChange: (id: String, isCheck: Boolean) -> Unit,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(bottomSheetHeight),
        shape = RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.padding(start = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = {
                        onCheckedChange(id, it)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        checkmarkColor = Color.Black,
                        uncheckedColor = Color.White
                    )
                )
                Text(text = "Deselect")
            }
            Button(onClick = onAdd) {
                Text(text = "Add ($selectedItemsCount)")
            }
        }
    }
}