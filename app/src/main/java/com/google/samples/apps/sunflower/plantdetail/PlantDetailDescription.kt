/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.plantdetail

import android.content.res.Configuration
import android.text.Layout.Alignment
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

//É aqui que vamos adicionar os códigos Compose

@Composable
fun PlantDetailDescription(plantDetailViewModel: PlantDetailViewModel) {
    /*
    método observeAsState: sempre que o liveData (classe que armazena estados, que nesse caso, está ligada
    à Plant) mudar o seu estado, o observeAsState pega esse valo e o atualiza, causando uma recomposição
     */
    val plant by plantDetailViewModel.plant.observeAsState()

    //Se "plant" não tiver valor nulo, chame o Composable "PlantDetailContent" que vai exibir o conteúdo
    plant?.let {
        PlantDetailContent(it)
    }
}

//Código do nome da planta
@Composable
private fun PlantName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .wrapContentWidth(align = androidx.compose.ui.Alignment.CenterHorizontally)
    )
}

//código das informações de irrigação da planta
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PlantWatering (wateringInterval: Int) {
    Column(Modifier.fillMaxWidth()) {
        val centroComPadding = Modifier
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .align(androidx.compose.ui.Alignment.CenterHorizontally)

        val PaddingNormal = dimensionResource(R.dimen.margin_normal)

        Text(
            text = stringResource(R.string.watering_needs_prefix),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold,
            modifier = centroComPadding.padding(top = PaddingNormal)
        )

        val intervaloRegarTexto = pluralStringResource(
            R.plurals.watering_needs_suffix,
            wateringInterval,
            wateringInterval
        )

        Text(
            text = intervaloRegarTexto,
            modifier = centroComPadding.padding(bottom = PaddingNormal)
        )
    }
}

/*
Código dos detalhes da planta, onde ela chama todos os outros composables que compõem as informações da planta
(nome, tempo de irrigação e descrição)
*/
@Composable
fun PlantDetailContent(plant: Plant) {
    Surface {
        Column(Modifier.padding(dimensionResource(R.dimen.margin_normal))) {
            PlantName(plant.name)
            PlantWatering(plant.wateringInterval)
            PlantDescription(plant.description)
        }
    }
}

//Código da descrição da planta
@Composable
private fun PlantDescription(description: String) {
    /*
    Como o Compose não pode renderizar código HTML (código no qual foi escrita a descrição)
    precisamos criar uma TextView usando a API AndroidView(que faz exatamente isso, cria Views)
    */
    /*
    Usa "remember" para guardar a informação de que o texto está formatado em HTML e o próprio
    texto, re-executa quando houver alguma mudança no texto
    */
    val htmlDescription = remember(description) {
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    AndroidView(
        //lambda no qual vai ser construida a TextView
        factory = { context ->
            //inicializando uma TextView que vai armazenar o texto em html
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        //lambda que é invocado quando há recomposições ou mudanças no texto
        update = {
            it.text = htmlDescription
        }
    )
}





@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlantDetailContentDarkPreview() {
    val plant = Plant("id", "Apple", "HTML<br><br>description", 3, 30, "")
    MdcTheme {
        PlantDetailContent(plant)
    }
}

@Preview
@Composable
private fun PlantDetailContentPreview() {
    val plant = Plant("id", "Apple", "HTML<br><br>description", 3, 30, "")
    MaterialTheme {
        PlantDetailContent(plant)
    }
}

@Preview
@Composable
private fun PlantDescriptionPreview() {
    MaterialTheme{
        PlantDescription("HTML<br><br>description")
    }
}

@Preview
@Composable
private fun PlantWateringPreview() {
    MaterialTheme{
        PlantWatering(7)
    }
}

@Preview
@Composable
fun PlantNamePreview() {
    MaterialTheme {
        PlantName("Apple")
    }
}