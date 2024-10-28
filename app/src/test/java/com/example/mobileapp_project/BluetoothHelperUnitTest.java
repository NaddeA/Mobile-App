import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class BluetoothHelperTest {
    private lateinit var context: Context
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothHelper: BluetoothHelper

    @Before
    fun setup() {
        context = mock(Context::class.java)
        bluetoothManager = mock(BluetoothManager::class.java)
        bluetoothAdapter = mock(BluetoothAdapter::class.java)

        // Mock the BluetoothManager and BluetoothAdapter
        `when`(context.getSystemService(Context.BLUETOOTH_SERVICE)).thenReturn(bluetoothManager)
        `when`(bluetoothManager.adapter).thenReturn(bluetoothAdapter)

        // Instantiate the BluetoothHelper
        bluetoothHelper = BluetoothHelper(context)
    }

    @Test
    fun testToggleBluetooth_enablesBluetoothWhenDisabled() {
        // Bluetooth is initially disabled
        `when`(bluetoothAdapter.isEnabled).thenReturn(false)
        bluetoothHelper.toggleBluetooth()

        // Verify that enabling Bluetooth was attempted
        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        verify(context).startActivity(enableIntent)
    }

    @Test
    fun testToggleBluetooth_doesNotEnableBluetoothWhenAlreadyEnabled() {
        // Bluetooth is already enabled
        `when`(bluetoothAdapter.isEnabled).thenReturn(true)
        bluetoothHelper.toggleBluetooth()

        // Verify that no attempt to enable Bluetooth was made
        verify(context, never()).startActivity(any(Intent::class.java))
    }

    @Test
    fun testDiscoverDevices_startsDiscovery() {
        // Bluetooth is enabled and permissions are granted
        `when`(bluetoothAdapter.isEnabled).thenReturn(true)
        `when`(ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN))
                .thenReturn(PackageManager.PERMISSION_GRANTED)
        `when`(bluetoothAdapter.startDiscovery()).thenReturn(true)

        bluetoothHelper.discoverDevices()

        // Verify that discovery was started
        verify(bluetoothAdapter).startDiscovery()
    }

    @Test
    fun testGetPairedDevices_returnsPairedDevices() {
        // Mock paired devices
        val mockDevice: BluetoothDevice = mock(BluetoothDevice::class.java)
        `when`(bluetoothAdapter.bondedDevices).thenReturn(setOf(mockDevice))

        // Bluetooth is enabled and permissions are granted
        `when`(bluetoothAdapter.isEnabled).thenReturn(true)
        `when`(ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT))
                .thenReturn(PackageManager.PERMISSION_GRANTED)

        val pairedDevices = bluetoothHelper.getPairedDevices()

        // Verify that the paired device is returned
        assertEquals(1, pairedDevices.size)
        assertEquals(mockDevice, pairedDevices[0])
    }
}
