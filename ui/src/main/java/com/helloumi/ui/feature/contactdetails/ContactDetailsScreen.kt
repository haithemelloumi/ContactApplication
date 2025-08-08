package com.helloumi.ui.feature.contactdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.LocationDomain
import com.helloumi.ui.R
import com.helloumi.ui.theme.Dimens
import com.helloumi.ui.theme.Dimens.CONTACT_DETAILS_ELEVATION
import com.helloumi.ui.theme.Dimens.CONTACT_DETAILS_ICON_SIZE
import com.helloumi.ui.theme.Dimens.CONTACT_DETAILS_IMAGE_SIZE
import com.helloumi.ui.theme.Dimens.CONTACT_ITEM_ROUNDED_SHAPE
import com.helloumi.ui.theme.Dimens.STACK_XXS
import com.helloumi.ui.theme.PINK_40
import com.helloumi.ui.theme.PINK_80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsScreen(
    modifier: Modifier = Modifier,
    contact: ContactDomain
) {
    ContactDetailsContent(
        modifier = modifier,
        contact = contact,
    )
}

@Composable
fun ContactDetailsContent(
    modifier: Modifier = Modifier,
    contact: ContactDomain
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.STACK_MD),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contact Picture
        AsyncImage(
            model = contact.picture,
            error = painterResource(R.drawable.contact_place_holder),
                    contentDescription = "Contact picture",
            modifier = modifier
                .size(CONTACT_DETAILS_IMAGE_SIZE)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(Dimens.STACK_LG))

        // Contact Name
        Text(
            text = "${contact.firstName} ${contact.lastName}",
            style = TextStyle(
                fontSize = Dimens.TEXT_SIZE_HEADLINE_MEDIUM,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(Dimens.STACK_MD))

        // Contact Details Cards
        ContactDetailCard(
            icon = Icons.Default.Email,
            title = stringResource(R.string.email),
            value = contact.email
        )

        Spacer(modifier = Modifier.height(Dimens.STACK_SM))

        ContactDetailCard(
            icon = Icons.Default.Phone,
            title = stringResource(R.string.phone),
            value = contact.phone
        )

        Spacer(modifier = Modifier.height(Dimens.STACK_SM))

        ContactDetailCard(
            icon = Icons.Default.LocationOn,
            title = stringResource(R.string.address),
            value = "${contact.location.street}\n${contact.location.city}, " +
                    "${contact.location.state} " +
                    "${contact.location.postcode}\n${contact.location.country}"
        )
    }
}

@Composable
fun ContactDetailCard(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CONTACT_ITEM_ROUNDED_SHAPE),
        colors = CardDefaults.cardColors(
            containerColor = PINK_80,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = CONTACT_DETAILS_ELEVATION)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.STACK_MD),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = modifier.size(CONTACT_DETAILS_ICON_SIZE),
                tint = PINK_40
            )

            Spacer(modifier = Modifier.width(Dimens.STACK_MD))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(STACK_XXS)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = Dimens.TEXT_SIZE_BIG,
                        fontWeight = FontWeight.Bold
                    ),
                    color = PINK_40
                )

                Text(
                    text = value,
                    style = TextStyle(
                        fontSize = Dimens.TEXT_SIZE_MEDIUM
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactDetailsPreview() {
    val sampleContact = ContactDomain(
        id = "1",
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com",
        phone = "+1234567890",
        picture = "https://randomuser.me/api/portraits/men/1.jpg",
        gender = "male",
        location = LocationDomain(
            street = "123 Main St",
            city = "New York",
            state = "NY",
            country = "USA",
            postcode = "10001"
        )
    )

    ContactDetailsScreen(
        contact = sampleContact
    )
}
