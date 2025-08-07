package com.helloumi.ui.feature.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.helloumi.domain.model.ContactDomain
import com.helloumi.ui.theme.Dimens
import com.helloumi.ui.theme.Dimens.CONTACT_ITEM_ELEVATION
import com.helloumi.ui.theme.Dimens.CONTACT_ITEM_IMAGE_SIZE
import com.helloumi.ui.theme.Dimens.CONTACT_ITEM_ROUNDED_SHAPE
import com.helloumi.ui.theme.Dimens.STACK_MD
import com.helloumi.ui.theme.Dimens.STACK_XXS
import com.helloumi.ui.theme.PINK_80
import com.helloumi.ui.theme.PURPLE_GREY_40

@Composable
fun ContactItem(
    contact: ContactDomain,
    onClickContact: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickContact() },
        shape = RoundedCornerShape(CONTACT_ITEM_ROUNDED_SHAPE),
        colors = CardDefaults.cardColors(
            containerColor = PINK_80,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = CONTACT_ITEM_ELEVATION)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(STACK_MD),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contact Picture
            AsyncImage(
                model = contact.picture,
                contentDescription = "Contact picture",
                modifier = Modifier
                    .size(CONTACT_ITEM_IMAGE_SIZE)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(STACK_MD))

            // Contact Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(STACK_XXS)
            ) {
                Text(
                    text = "${contact.firstName} ${contact.lastName}",
                    style = TextStyle(
                        fontSize = Dimens.TEXT_SIZE_BIG,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = contact.email,
                    style = TextStyle(
                        fontSize = Dimens.TEXT_SIZE_MEDIUM,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = contact.phone,
                    style = TextStyle(
                        fontSize = Dimens.TEXT_SIZE_MEDIUM,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    val sampleContact = ContactDomain(
        id = "1",
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com",
        phone = "+1234567890",
        picture = "https://randomuser.me/api/portraits/men/1.jpg",
        gender = "male",
        location = com.helloumi.domain.model.LocationDomain(
            street = "123 Main St",
            city = "New York",
            state = "NY",
            country = "USA",
            postcode = "10001"
        )
    )

    ContactItem(
        contact = sampleContact,
        onClickContact = {}
    )
}
