import React, { useState, useEffect, useContext } from 'react';
import styled from 'styled-components';
import { AuthContext } from '../context/AuthContext';
import PageWithNavbar from '../components/PageWithNavbar';
import HomeDetailsModal from '../components/HomeDetailsModal';
import { Icon } from '@iconify/react';
import timezoneData from '../data/timezones.json';

const Container = styled.div`
    padding: 2rem;
`;

const Grid = styled.div`
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 1.5rem;
    margin-top: 1.5rem;

    @media (max-width: 768px) {
        grid-template-columns: 1fr;
    }
`;

const HomeCard = styled.div`
    background-color: #f9f9f9;
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 1rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    text-align: center;

    &:hover {
        box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
    }
`;

const HomeTitle = styled.h3`
    font-size: 1.25rem;
    margin: 0.5rem 0;
`;

const Address = styled.p`
    color: #555;
    font-size: 0.9rem;
    margin: 0.5rem 0;
`;

const AddButton = styled.button`
    background-color: #4caf50;
    color: white;
    border: none;
    padding: 0.75rem 1.5rem;
    border-radius: 5px;
    font-size: 1rem;
    cursor: pointer;
    margin: 1rem 0;

    &:hover {
        background-color: #45a049;
    }
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

const ModalContent = styled.div`
    background-color: white;
    padding: 2rem;
    border-radius: 8px;
    width: 90%;
    max-width: 500px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
`;

const FormGroup = styled.div`
    margin-bottom: 1rem;
`;

const Label = styled.label`
    display: block;
    margin-bottom: 0.5rem;
    font-weight: bold;
`;

const Input = styled.input`
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
`;

const Select = styled.select`
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
`;

const ModalActions = styled.div`
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
`;

const ActionButtons = styled.div`
    display: flex;
    justify-content: center;
    gap: 0.5rem;
    margin-top: 0.5rem;
`;

const IconButton = styled.button`
    background: none;
    border: none;
    cursor: pointer;
    padding: 0.25rem;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    &:hover {
        background-color: rgba(0, 0, 0, 0.05);
    }

    svg {
        color: ${props => props.delete ? '#dc3545' : '#007bff'};
    }
`;

const ErrorMessage = styled.div`
    color: #dc3545;
    font-size: 0.875rem;
    margin-top: 0.25rem;
`;

const Home = ({ home, onClick, onEdit, onDelete }) => {
    return (
        <HomeCard>
            <div onClick={() => onClick(home)}>
                <Icon icon="mdi:home-outline" width="40" height="40" style={{ color: '#4caf50' }} />
                <HomeTitle>{home.name}</HomeTitle>
                <Address>{home.address}</Address>
                <p>Time Zone: {home.timeZone}</p>
            </div>
            <ActionButtons>
                <IconButton onClick={(e) => {
                    e.stopPropagation();
                    onEdit(home);
                }}>
                    <Icon icon="mdi:pencil" width="20" height="20" />
                </IconButton>
                <IconButton delete onClick={(e) => {
                    e.stopPropagation();
                    onDelete(home);
                }}>
                    <Icon icon="mdi:trash-can" width="20" height="20" />
                </IconButton>
            </ActionButtons>
        </HomeCard>
    );
};

const HomesPage = () => {
    const [homes, setHomes] = useState([]);
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [selectedHome, setSelectedHome] = useState(null);
    const [newHome, setNewHome] = useState({ name: '', address: '', timeZone: '' });
    const [timeZones, setTimeZones] = useState([]);
    const { user } = useContext(AuthContext);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [homeToEdit, setHomeToEdit] = useState(null);
    const [homeToDelete, setHomeToDelete] = useState(null);
    const [validationErrors, setValidationErrors] = useState({});

    useEffect(() => {
        const token = localStorage.getItem('token');

        if (token) {
            fetch('http://localhost:8080/api/homes', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch homes');
                    }
                    return response.json();
                })
                .then((data) => setHomes(data))
                .catch((error) => console.error('Error:', error));
        }

        setTimeZones(timezoneData.timezones);
    }, [user]);

    const handleAddHome = () => {
        const token = localStorage.getItem('token');
        if (token) {
            fetch('http://localhost:8080/api/homes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(newHome),
            })
                .then(async (response) => {
                    if (!response.ok) {
                        if (response.status === 400) {
                            const errorData = await response.json();
                            setValidationErrors(errorData);
                            throw new Error('Validation failed');
                        }
                        throw new Error('Failed to add home');
                    }
                    return response.json();
                })
                .then((data) => {
                    setHomes((prevHomes) => [...prevHomes, data]);
                    setIsAddModalOpen(false);
                    setNewHome({ name: '', address: '', timeZone: '' });
                    setValidationErrors({});
                })
                .catch((error) => {
                    if (error.message !== 'Validation failed') {
                        console.error('Error:', error);
                        setValidationErrors({ general: 'Failed to add home. Please try again.' });
                    }
                });
        }
    };

    const handleHomeClick = (home) => {
        const token = localStorage.getItem('token');
        if (token) {
            fetch(`http://localhost:8080/api/homes/${home.id}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch home details');
                    }
                    return response.json();
                })
                .then((data) => setSelectedHome(data))
                .catch((error) => console.error('Error:', error));
        }
    };

    const handleUpdateDevice = (roomId, deviceId, updatedDevice) => {
        // Update the device in the selected home's state
        const updatedHome = { ...selectedHome };
        updatedHome.rooms = updatedHome.rooms.map(room => {
            if (room.id === roomId) {
                room.devices = room.devices.map(device => 
                    device.id === deviceId ? updatedDevice : device
                );
            }
            return room;
        });
        setSelectedHome(updatedHome);
    };

    const handleEditHome = (home) => {
        setHomeToEdit(home);
        setNewHome({
            name: home.name,
            address: home.address,
            timeZone: home.timeZone
        });
        setValidationErrors({});
        setIsEditModalOpen(true);
    };

    const handleDeleteHome = (home) => {
        setHomeToDelete(home);
        setIsDeleteModalOpen(true);
    };

    const confirmEdit = () => {
        const token = localStorage.getItem('token');
        if (token) {
            fetch(`http://localhost:8080/api/homes/${homeToEdit.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(newHome),
            })
                .then(async (response) => {
                    if (!response.ok) {
                        if (response.status === 400) {
                            const errorData = await response.json();
                            setValidationErrors(errorData);
                            throw new Error('Validation failed');
                        }
                        throw new Error('Failed to update home');
                    }
                    return response.json();
                })
                .then((updatedHome) => {
                    setHomes(prevHomes => 
                        prevHomes.map(home => 
                            home.id === updatedHome.id ? updatedHome : home
                        )
                    );
                    setIsEditModalOpen(false);
                    setHomeToEdit(null);
                    setNewHome({ name: '', address: '', timeZone: '' });
                    setValidationErrors({});
                })
                .catch((error) => {
                    if (error.message !== 'Validation failed') {
                        console.error('Error:', error);
                        setValidationErrors({ general: 'Failed to update home. Please try again.' });
                    }
                });
        }
    };

    const confirmDelete = () => {
        const token = localStorage.getItem('token');
        if (token) {
            fetch(`http://localhost:8080/api/homes/${homeToDelete.id}`, {
                method: 'DELETE',
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Failed to delete home');
                    }
                    setHomes(prevHomes => 
                        prevHomes.filter(home => home.id !== homeToDelete.id)
                    );
                    setIsDeleteModalOpen(false);
                    setHomeToDelete(null);
                })
                .catch((error) => console.error('Error:', error));
        }
    };

    const handleUpdateHome = (updatedHome) => {
        setSelectedHome(updatedHome);
        // Also update the home in the homes list
        setHomes(prevHomes => 
            prevHomes.map(home => 
                home.id === updatedHome.id ? updatedHome : home
            )
        );
    };

    return (
        <PageWithNavbar>
            <Container>
                <h1>Your Homes</h1>
                <AddButton onClick={() => setIsAddModalOpen(true)}>Add New Home</AddButton>
                {homes.length > 0 ? (
                    <Grid>
                        {homes.map((home) => (
                            <Home
                                key={home.id}
                                home={home}
                                onClick={handleHomeClick}
                                onEdit={handleEditHome}
                                onDelete={handleDeleteHome}
                            />
                        ))}
                    </Grid>
                ) : (
                    <p>No homes available.</p>
                )}

                {isAddModalOpen && (
                    <ModalOverlay>
                        <ModalContent>
                            <h2>Add New Home</h2>
                            <FormGroup>
                                <Label htmlFor="name">Name</Label>
                                <Input
                                    id="name"
                                    type="text"
                                    value={newHome.name}
                                    onChange={(e) => setNewHome({ ...newHome, name: e.target.value })}
                                />
                                {validationErrors.name && (
                                    <ErrorMessage>{validationErrors.name}</ErrorMessage>
                                )}
                            </FormGroup>
                            <FormGroup>
                                <Label htmlFor="address">Address</Label>
                                <Input
                                    id="address"
                                    type="text"
                                    value={newHome.address}
                                    onChange={(e) => setNewHome({ ...newHome, address: e.target.value })}
                                />
                                {validationErrors.address && (
                                    <ErrorMessage>{validationErrors.address}</ErrorMessage>
                                )}
                            </FormGroup>
                            <FormGroup>
                                <Label htmlFor="timeZone">Time Zone</Label>
                                <Select
                                    id="timeZone"
                                    value={newHome.timeZone}
                                    onChange={(e) => setNewHome({ ...newHome, timeZone: e.target.value })}
                                >
                                    <option value="">Select a time zone</option>
                                    {timeZones.map((tz) => (
                                        <option key={tz} value={tz}>
                                            {tz}
                                        </option>
                                    ))}
                                </Select>
                                {validationErrors.timeZone && (
                                    <ErrorMessage>{validationErrors.timeZone}</ErrorMessage>
                                )}
                            </FormGroup>
                            {validationErrors.general && (
                                <ErrorMessage style={{ marginBottom: '1rem' }}>{validationErrors.general}</ErrorMessage>
                            )}
                            <ModalActions>
                                <button onClick={() => {
                                    setIsAddModalOpen(false);
                                    setNewHome({ name: '', address: '', timeZone: '' });
                                    setValidationErrors({});
                                }}>Cancel</button>
                                <button onClick={handleAddHome}>Add</button>
                            </ModalActions>
                        </ModalContent>
                    </ModalOverlay>
                )}

                {isEditModalOpen && (
                    <ModalOverlay>
                        <ModalContent>
                            <h2>Edit Home</h2>
                            <FormGroup>
                                <Label htmlFor="name">Name</Label>
                                <Input
                                    id="name"
                                    type="text"
                                    value={newHome.name}
                                    onChange={(e) => setNewHome({ ...newHome, name: e.target.value })}
                                />
                                {validationErrors.name && (
                                    <ErrorMessage>{validationErrors.name}</ErrorMessage>
                                )}
                            </FormGroup>
                            <FormGroup>
                                <Label htmlFor="address">Address</Label>
                                <Input
                                    id="address"
                                    type="text"
                                    value={newHome.address}
                                    onChange={(e) => setNewHome({ ...newHome, address: e.target.value })}
                                />
                                {validationErrors.address && (
                                    <ErrorMessage>{validationErrors.address}</ErrorMessage>
                                )}
                            </FormGroup>
                            <FormGroup>
                                <Label htmlFor="timeZone">Time Zone</Label>
                                <Select
                                    id="timeZone"
                                    value={newHome.timeZone}
                                    onChange={(e) => setNewHome({ ...newHome, timeZone: e.target.value })}
                                >
                                    <option value="">Select a time zone</option>
                                    {timeZones.map((tz) => (
                                        <option key={tz} value={tz}>
                                            {tz}
                                        </option>
                                    ))}
                                </Select>
                                {validationErrors.timeZone && (
                                    <ErrorMessage>{validationErrors.timeZone}</ErrorMessage>
                                )}
                            </FormGroup>
                            {validationErrors.general && (
                                <ErrorMessage style={{ marginBottom: '1rem' }}>{validationErrors.general}</ErrorMessage>
                            )}
                            <ModalActions>
                                <button onClick={() => {
                                    setIsEditModalOpen(false);
                                    setHomeToEdit(null);
                                    setNewHome({ name: '', address: '', timeZone: '' });
                                    setValidationErrors({});
                                }}>Cancel</button>
                                <button onClick={confirmEdit}>Save Changes</button>
                            </ModalActions>
                        </ModalContent>
                    </ModalOverlay>
                )}

                {isDeleteModalOpen && (
                    <ModalOverlay>
                        <ModalContent>
                            <h2>Delete Home</h2>
                            <p>Are you sure you want to delete {homeToDelete?.name}? This action cannot be undone.</p>
                            <ModalActions>
                                <button onClick={() => {
                                    setIsDeleteModalOpen(false);
                                    setHomeToDelete(null);
                                }}>Cancel</button>
                                <button 
                                    onClick={confirmDelete}
                                    style={{ backgroundColor: '#dc3545', color: 'white' }}
                                >
                                    Delete
                                </button>
                            </ModalActions>
                        </ModalContent>
                    </ModalOverlay>
                )}

                {/* Home Details Modal */}
                {selectedHome && (
                    <HomeDetailsModal 
                        home={selectedHome} 
                        onClose={() => setSelectedHome(null)}
                        onUpdateDevice={handleUpdateDevice}
                        onUpdateHome={handleUpdateHome}
                    />
                )}
            </Container>
        </PageWithNavbar>
    );
};


export default HomesPage;
