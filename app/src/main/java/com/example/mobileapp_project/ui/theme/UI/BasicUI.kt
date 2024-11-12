//package com.example.mobileapp_project.ui.theme.UI
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.bluetooth.BluetoothDevice
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Info
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.*
//import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothDeviceDomain
//import com.example.mobileapp_project.BluetoothHelper
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//sealed class Screen {
//    object MainMenu: Screen()
//    object BtDiscovered : Screen()
//    object BtPaired : Screen()
//}
//
//@Composable
//fun AppScreen(BTH: BluetoothHelper, activity: Activity,onDeviceFound: (BluetoothDeviceDomain) -> Unit) {
//    // Track the current screen
//    var currentScreen by remember { mutableStateOf<Screen>(Screen.MainMenu) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        when (currentScreen) {
//            is Screen.MainMenu -> MainMenuScreen ( {selectedScreen ->
//                currentScreen = selectedScreen},
//                {BTH.enableBluetooth(activity)}
//            )
//            is Screen.BtDiscovered -> BtDiscoveredScreen (bluetoothHelper = BTH,
//                onNavigate = { selectedScreen -> currentScreen = selectedScreen },
//                onDiscoverDevices = { BTH.discoverDevices(activity,onDeviceFound(BluetoothDeviceDomain)) }
//                )
//            is Screen.BtPaired -> BtPairedScreen (
//                onNavigate = { selectedScreen -> currentScreen = selectedScreen },
//                bluetoothHelper = BTH)
//        }
//    }
//}
//
//
//@Composable
//fun MainMenuScreen(onNavigate: (Screen) -> Unit,
//                   onBluetoothToggle: () -> Unit) {
//    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = "Main Menu")
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = { onNavigate(Screen.BtDiscovered) }) {
//            Text("Go to Discovered Devices")
//        }
//        Button(onClick = onBluetoothToggle) {
//            Text("Toggle Bluetooth")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = { onNavigate(Screen.BtPaired) }) {
//            Text("Go to Paired Devices")
//        }
//
//    }
//}
//
//@SuppressLint("MissingPermission")
//@Composable
//fun BtDiscoveredScreen(onNavigate: (Screen) -> Unit,
//                       bluetoothHelper: BluetoothHelper,
//                       onDiscoverDevices: () -> Unit) {
//    val discoveredDevices = remember { mutableStateListOf<BluetoothDevice>() }
//    val coroutineScope = rememberCoroutineScope()
//    // Effect to handle receiver registration and cleanup
//    DisposableEffect(Unit) {
//        val receiver = bluetoothHelper.createDeviceDiscoveryReceiver { device ->
//            // Add the device to the list, updating the UI
//            discoveredDevices.add(device)
//        }
//
//        onDispose {
//            // Unregister the receiver when this composable leaves the composition
//            bluetoothHelper.unregisterReceiver(receiver)
//        }
//    }
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button({ discoveredDevices.clear()
//            onDiscoverDevices()}
//        ) {
//            Text("Discover Devices")
//        }
//        Text(text = "Discovered Bluetooth Devices")
//
//        // LazyColumn to display the list of discovered devices
//        LazyColumn {
//            items(discoveredDevices) { device ->
//                BluetoothDeviceItem(
//                    device = device,
//                    onDeviceClick = { device ->
//                        coroutineScope.launch {
//                            bluetoothHelper.connectToDevice(device)
//                        }
//                    }
//
//                )
//
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = { onNavigate(Screen.MainMenu) }) {
//            Text("Back to Main Menu")
//        }
//    }
//    // Update the list with newly discovered devices from BluetoothHelper
//    DisposableEffect(Unit) {
//        val receiver = bluetoothHelper.createDeviceDiscoveryReceiver { device ->
//            discoveredDevices.add(device)
//        }
//        onDispose {
//            bluetoothHelper.unregisterReceiver(receiver)
//        }
//    }
//}
//
//@Composable
//fun BtPairedScreen(
//    bluetoothHelper: BluetoothHelper,
//    onNavigate: (Screen) -> Unit,
//
//) {
//    // Fetch the list of paired devices from BluetoothHelper
//    val pairedDevices = bluetoothHelper.getPairedDevices()
//    val coroutineScope = rememberCoroutineScope()
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = "Paired Bluetooth Devices", style = MaterialTheme.typography.headlineSmall)
//        Spacer(modifier = Modifier.height(8.dp))
//
//        LazyColumn {
//            items(pairedDevices) { device ->
//                BluetoothDeviceItem(
//                    device = device,
//                    onDeviceClick = { device ->
//                        coroutineScope.launch {
//                            bluetoothHelper.connectToDevice(device)
//                        }
//                    }
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { onNavigate(Screen.MainMenu) }) {
//            Text("Back to Main Menu")
//        }
//    }
//}
//
//
//@SuppressLint("MissingPermission")
//@Composable
//fun DeviceListScreen(
//    deviceNames: List<BluetoothDeviceDomain>,
//    onDeviceClick: (String) -> Unit
//) {
//    LazyColumn {
//        items(deviceNames) { deviceName ->
//            Text(
//                text = deviceName.name ?: "unknown",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .clickable { onDeviceClick(deviceName.toString()) }
//                ,
//                style = MaterialTheme.typography.bodyLarge // Adjusted for better visibility
//            )
//            HorizontalDivider() // Adds a line between items
//        }
//    }
//}
//
//@SuppressLint("MissingPermission")
//@Composable
//fun BluetoothScreen(bluetoothHelper: BluetoothHelper) {
//    val pairedDevices = remember { mutableStateOf(listOf<BluetoothDevice>()) }
//    val discoveredDevices = remember { mutableStateOf(listOf<BluetoothDevice>()) }
//
//    // Function to update devices periodically or on specific action
//    fun updateDeviceList() {
//        pairedDevices.value = bluetoothHelper.getPairedDevices()
//        discoveredDevices.value = bluetoothHelper.getDiscoveredDevices()
//    }
//
//    // Set up UI with paired and discovered devices
//    Column {
//        Text("Paired Devices:")
//        LazyColumn {
//            items(pairedDevices.value) { device ->
//                Text(text = device.name ?: "Unknown Device", modifier = Modifier.clickable {
//                    // Trigger connection to device
//                })
//            }
//        }
//        Text("Discovered Devices:")
//        LazyColumn {
//            items(discoveredDevices.value) { device ->
//                Text(text = device.name ?: "Unknown Device", modifier = Modifier.clickable {
//                    // Trigger connection to device
//                })
//            }
//        }
//        // Trigger discovery or updates on button press
//        Button(onClick = {
//            bluetoothHelper.discoverDevices()
//            updateDeviceList()
//        }) {
//            Text("Discover Devices")
//        }
//    }
//}
//
//@Composable
//fun DeviceDiscovery(bluetoothHelper: BluetoothHelper) {
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(5000)  // Adjust as needed for your refresh rate
//            bluetoothHelper.discoverDevices()
//        }
//    }
//    BluetoothScreen(bluetoothHelper)
//}
//
//@SuppressLint("MissingPermission")
//@Composable
//fun BluetoothDeviceItem(
//    device: BluetoothDevice,
//    onDeviceClick: (BluetoothDevice) -> Unit
//) {
//    ListItem(
//        headlineContent = {
//            Text(
//                text = device.name ?: "Unknown Device",
//                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, fontSize = 16.sp)
//            )
//        },
//        leadingContent = {
//            Icon(
//                imageVector = Icons.Default.Info ,
//                contentDescription = "Bluetooth Device Icon",
//                tint = Color.Blue
//            )
//        },
//        modifier = Modifier
//            .clickable { onDeviceClick(device) }
//            .padding(4.dp)
//    )
//}
//
//
//
//@Composable
//fun MainScreen(
//    onBluetoothToggle: () -> Unit,
//    onDiscoverDevices: () -> Unit,
//    onGetPairedDevices: () -> Unit,
//    onDiscoverability: () -> Unit,
//    discoveredDevices: List<BluetoothDevice>,
//    pairedDevices: List<BluetoothDevice>
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(onClick = onBluetoothToggle) {
//            Text("Toggle Bluetooth")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = onDiscoverDevices) {
//            Text("Discover Devices")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = onGetPairedDevices) {
//            Text("Get Paired Devices")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = onDiscoverability) {
//            Text("Make Discoverable")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Discovered Devices:")
//        DeviceListScreen(deviceNames = discoveredDevices) { selectedDevice ->
//            // Handle discovered device click
//            println("Discovered Device clicked: $selectedDevice")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        HorizontalDivider()
//
//        Text("Paired Devices:")
//        DeviceListScreen(deviceNames = pairedDevices) { selectedDevice   ->
//            // Handle paired device click
//            println("Paired Device clicked: $selectedDevice")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//
//    }
//}
//*/