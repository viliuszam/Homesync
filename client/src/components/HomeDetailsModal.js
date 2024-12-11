import React, { useState } from 'react';
import styled from 'styled-components';
import { Icon } from '@iconify/react';
import RoomFormModal from './RoomFormModal';
import DeviceFormModal from './DeviceFormModal';

const DetailModalOverlay = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
`;

const DetailModalContent = styled.div`
    background-color: white;
    padding: 2rem;
    border-radius: 8px;
    width: 90%;
    max-width: 500px;
    max-height: 80vh;
    overflow-y: auto;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
`;

const RoomSection = styled.div`
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-bottom: 1rem;
`;

const RoomHeader = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem;
    background-color: #f4f4f4;
    cursor: pointer;
`;

const DeviceList = styled.div`
    padding: 1rem;
`;

const ModalOverlay = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
`;

const DeviceCard = styled.div`
    background-color: #f9f9f9;
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    padding: 1rem;
    margin-bottom: 0.5rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
`;

const DeviceStateToggle = styled.button`
    background-color: ${props => props.isOn ? '#4caf50' : '#f44336'};
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 4px;
    cursor: pointer;
`;

const ModalActions = styled.div`
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 1rem;
`;

const ActionButton = styled.button`
    background: none;
    border: none;
    cursor: pointer;
    color: ${props => props.delete ? '#dc3545' : '#007bff'};
    padding: 0.25rem;
    margin-left: 0.5rem;

    &:hover {
        text-decoration: underline;
    }
`;

const AddButton = styled.button`
    background-color: #4caf50;
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 4px;
    cursor: pointer;
    margin-bottom: 1rem;

    &:hover {
        background-color: #45a049;
    }
`;

const HomeDetailsModal = ({ home, onClose, onUpdateDevice, onUpdateHome }) => {
    const [openRooms, setOpenRooms] = useState({});
    const [showRoomModal, setShowRoomModal] = useState(false);
    const [showDeviceModal, setShowDeviceModal] = useState(false);
    const [selectedRoom, setSelectedRoom] = useState(null);
    const [selectedDevice, setSelectedDevice] = useState(null);
    const [currentRoomId, setCurrentRoomId] = useState(null);

    const toggleRoom = (roomId) => {
        setOpenRooms(prev => ({
            ...prev,
            [roomId]: !prev[roomId]
        }));
    };

    const handleDeviceStateUpdate = (roomId, deviceId, type, deviceName, manuf, currentState) => {
        // Prepare the update payload
        const updatePayload = {
            state: !currentState,
            deviceType: type,
            name: deviceName,
            manufacturer: manuf
        };
            console.log(roomId + " " + deviceId + " " + currentState);
        const token = localStorage.getItem('token');
        if (token) {
            fetch(`http://localhost:8080/api/devices/${deviceId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(updatePayload)
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Failed to update device state');
                    }
                    return response.json();
                })
                .then((updatedDevice) => {
                    // Call the parent component's update method
                    onUpdateDevice(roomId, deviceId, updatedDevice);
                })
                .catch((error) => console.error('Error:', error));
        }
    };

    const handleCreateRoom = async (roomData) => {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/rooms?homeId=${home.id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(roomData),
        });

        if (!response.ok) {
            const error = await response.json();
            throw { validationErrors: error };
        }
        
        // Fetch updated home data after creating room
        const updatedHomeResponse = await fetch(`http://localhost:8080/api/homes/${home.id}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        const updatedHome = await updatedHomeResponse.json();
        onUpdateHome(updatedHome);
    };

    const handleUpdateRoom = async (roomData) => {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/rooms/${selectedRoom.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(roomData),
        });

        if (!response.ok) {
            const error = await response.json();
            throw { validationErrors: error };
        }

        // Fetch updated home data after updating room
        const updatedHomeResponse = await fetch(`http://localhost:8080/api/homes/${home.id}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        const updatedHome = await updatedHomeResponse.json();
        onUpdateHome(updatedHome);
    };

    const handleDeleteRoom = async (roomId) => {
        if (window.confirm('Are you sure you want to delete this room?')) {
            const token = localStorage.getItem('token');
            const response = await fetch(`http://localhost:8080/api/rooms/${roomId}`, {
                method: 'DELETE',
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                // Fetch updated home data after deleting room
                const updatedHomeResponse = await fetch(`http://localhost:8080/api/homes/${home.id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                const updatedHome = await updatedHomeResponse.json();
                onUpdateHome(updatedHome);
            }
        }
    };

    const handleCreateDevice = async (deviceData) => {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/devices?roomId=${currentRoomId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(deviceData),
        });

        if (!response.ok) {
            const error = await response.json();
            throw { validationErrors: error };
        }

        // Fetch updated home data after creating device
        const updatedHomeResponse = await fetch(`http://localhost:8080/api/homes/${home.id}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        const updatedHome = await updatedHomeResponse.json();
        onUpdateHome(updatedHome);
    };

    const handleUpdateDevice = async (deviceData) => {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/devices/${selectedDevice.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(deviceData),
        });

        if (!response.ok) {
            const error = await response.json();
            throw { validationErrors: error };
        }

        // Fetch updated home data after updating device
        const updatedHomeResponse = await fetch(`http://localhost:8080/api/homes/${home.id}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        const updatedHome = await updatedHomeResponse.json();
        onUpdateHome(updatedHome);
    };

    const handleDeleteDevice = async (deviceId) => {
        if (window.confirm('Are you sure you want to delete this device?')) {
            const token = localStorage.getItem('token');
            const response = await fetch(`http://localhost:8080/api/devices/${deviceId}`, {
                method: 'DELETE',
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                // Fetch updated home data after deleting device
                const updatedHomeResponse = await fetch(`http://localhost:8080/api/homes/${home.id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                const updatedHome = await updatedHomeResponse.json();
                onUpdateHome(updatedHome);
            }
        }
    };

    return (
        <DetailModalOverlay onClick={onClose}>
            <DetailModalContent onClick={(e) => e.stopPropagation()}>
                <h2>{home.name}</h2>
                <p>Address: {home.address}</p>
                <p>Time Zone: {home.timeZone}</p>
                
                <h3>Rooms</h3>
                <AddButton onClick={() => setShowRoomModal(true)}>Add Room</AddButton>
                
                {home.rooms.length === 0 ? (
                    <p>No rooms added to this home yet.</p>
                ) : (
                    home.rooms.map((room) => (
                        <RoomSection key={room.id}>
                            <RoomHeader onClick={() => toggleRoom(room.id)}>
                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                    <h4>{room.name} ({room.roomType})</h4>
                                    <ActionButton onClick={(e) => {
                                        e.stopPropagation();
                                        setSelectedRoom(room);
                                        setShowRoomModal(true);
                                    }}>
                                        <Icon icon="mdi:pencil" />
                                    </ActionButton>
                                    <ActionButton delete onClick={(e) => {
                                        e.stopPropagation();
                                        handleDeleteRoom(room.id);
                                    }}>
                                        <Icon icon="mdi:trash-can" />
                                    </ActionButton>
                                </div>
                                <Icon 
                                    icon={openRooms[room.id] ? 'mdi:chevron-up' : 'mdi:chevron-down'} 
                                    width="24" 
                                    height="24" 
                                />
                            </RoomHeader>
                            {openRooms[room.id] && (
                                <DeviceList>
                                    <AddButton onClick={() => {
                                        setCurrentRoomId(room.id);
                                        setShowDeviceModal(true);
                                    }}>
                                        Add Device
                                    </AddButton>
                                    {room.devices.length === 0 ? (
                                        <p>No devices in this room.</p>
                                    ) : (
                                        room.devices.map((device) => (
                                            <DeviceCard key={device.id}>
                                                <div>
                                                    <strong>{device.name}</strong>
                                                    <p>{device.deviceType} - {device.manufacturer}</p>
                                                    <div>
                                                        <ActionButton onClick={() => {
                                                            setSelectedDevice(device);
                                                            setShowDeviceModal(true);
                                                        }}>
                                                            <Icon icon="mdi:pencil" />
                                                        </ActionButton>
                                                        <ActionButton delete onClick={() => handleDeleteDevice(device.id)}>
                                                            <Icon icon="mdi:trash-can" />
                                                        </ActionButton>
                                                    </div>
                                                </div>
                                                <DeviceStateToggle 
                                                    isOn={device.state}
                                                    onClick={() => handleDeviceStateUpdate(room.id, device.id, device.deviceType, device.name, device.manufacturer, device.state)}
                                                >
                                                    {device.state ? 'ON' : 'OFF'}
                                                </DeviceStateToggle>
                                            </DeviceCard>
                                        ))
                                    )}
                                </DeviceList>
                            )}
                        </RoomSection>
                    ))
                )}

                {showRoomModal && (
                    <ModalOverlay onClick={() => setShowRoomModal(false)}>
                        <RoomFormModal
                            room={selectedRoom}
                            onClose={() => {
                                setShowRoomModal(false);
                                setSelectedRoom(null);
                            }}
                            onSubmit={selectedRoom ? handleUpdateRoom : handleCreateRoom}
                            title={selectedRoom ? 'Edit Room' : 'Add Room'}
                        />
                    </ModalOverlay>
                )}

                {showDeviceModal && (
                    <ModalOverlay onClick={() => setShowDeviceModal(false)}>
                        <DeviceFormModal
                            device={selectedDevice}
                            onClose={() => {
                                setShowDeviceModal(false);
                                setSelectedDevice(null);
                            }}
                            onSubmit={selectedDevice ? handleUpdateDevice : handleCreateDevice}
                            title={selectedDevice ? 'Edit Device' : 'Add Device'}
                        />
                    </ModalOverlay>
                )}

                <ModalActions>
                    <button onClick={onClose}>Close</button>
                </ModalActions>
            </DetailModalContent>
        </DetailModalOverlay>
    );
};

export default HomeDetailsModal;