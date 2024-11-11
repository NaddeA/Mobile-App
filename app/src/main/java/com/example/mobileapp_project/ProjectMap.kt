package com.example.mobileapp_project

// planing File just made to visualize stuff

class ProjectMap {
    inner class Master{
        fun toggleBluetooth(){
            // Exists > could be automatic
        }
        fun discoverBTDevices(){
            // Exists > fix calls
        }
        fun connectToBTDevice(){
            // Exists > check code and edit accordingly
            // need to keep socket for data sending
        }
        fun listenToSlave(){
            // should be available > check
        }

        inner class TerminalCmd{
            // this is the class to map commands to buttons (could also e a drop down list) that sends to Slave in a specific format
            // might be a good idea to let this class handel the interpretation of the responses from Slave
        }
        fun sendCommand(){
            // maybe an extension of the TerminalCmd But byte stream and bluetooth socket are needed
        }
        fun displayResults(){
            // this could be sensor data/ Status ..

        }

    }
    inner class Slave{
        fun makeDiscoverable(){
            // Exists > check bluetoothHelper
        }
        fun sensorData(){
            // information with nahed
        }
        fun listenForCommands(){
            // Bluetooth related
        }
        inner class InterprateTerminalCmds{
            // this could copy paste in a reverse manner to that of TerminalCmd > this is to interprate the cmds and find out what format to use when sending back

        }
        fun sendResponse(){
            // related to terminal cmd
        }
        fun logData(){

        }

    }
}