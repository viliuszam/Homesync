import React, { useState, useContext, useEffect } from 'react';
import styled from 'styled-components';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import PageWithNavbar from '../components/PageWithNavbar';
import HomeDetailsModal from '../components/HomeDetailsModal';
import { Icon } from '@iconify/react';
import timezoneData from '../data/timezones.json';
import { AnimatedModalOverlay, AnimatedModalContent } from '../styles/modalStyles';

const Container = styled.div`
    padding: 2rem;
`;

const HeaderContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 2rem;
    text-align: center;

    h1 {
        margin-bottom: 1rem;
    }
`;

const SearchSection = styled.div`
    margin-bottom: 1rem;
    display: flex;
    gap: 1rem;
    align-items: center;
    justify-content: center;
`;

const Input = styled.input`
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;
    width: 300px;
`;

const SearchButton = styled.button`
    background-color: #007acc;
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 4px;
    cursor: pointer;
    font-size: 1rem;

    &:hover {
        background-color: #0056b3;
    }
`;

const ErrorMessage = styled.div`
    color: #dc3545;
    margin-top: 1rem;
`;

// Reuse the styled components from HomesPage.js
const Grid = styled.div`
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 1.5rem;
    margin-top: 1.5rem;
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

const ModalActions = styled.div`
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 1rem;
`;

const FormGroup = styled.div`
    margin-bottom: 1rem;
`;

const Label = styled.label`
    display: block;
    margin-bottom: 0.5rem;
    font-weight: bold;
`;

const Select = styled.select`
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
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

    &:disabled {
        background-color: #cccccc;
        cursor: not-allowed;
    }
`;

const AdminPanel = () => {
    const { user } = useContext(AuthContext);
    const [searchUsername, setSearchUsername] = useState('');
    const [homes, setHomes] = useState([]);
    const [error, setError] = useState('');
    const [selectedHome, setSelectedHome] = useState(null);
    const [loading, setLoading] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [homeToEdit, setHomeToEdit] = useState(null);
    const [homeToDelete, setHomeToDelete] = useState(null);
    const [newHome, setNewHome] = useState({ name: '', address: '', timeZone: '' });
    const [validationErrors, setValidationErrors] = useState({});
    const [timeZones, setTimeZones] = useState([]);
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [lastSearchedUser, setLastSearchedUser] = useState('');

    useEffect(() => {
        setTimeZones(timezoneData.timezones);
    }, []);

    // Redirect if not admin
    if (!user || user.role !== 'ADMINISTRATOR') {
        return <Navigate to="/homes" replace />;
    }

    const handleSearch = async () => {
        if (!searchUsername.trim()) {
            setError('Please enter a username');
            return;
        }

        setLoading(true);
        setError('');

        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`http://localhost:8080/api/homes/name/${searchUsername}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                throw new Error(response.status === 404 ? 'User not found' : 'Failed to fetch homes');
            }

            const data = await response.json();
            setHomes(data);
            setLastSearchedUser(searchUsername);
        } catch (err) {
            setError(err.message);
            setHomes([]);
            setLastSearchedUser('');
        } finally {
            setLoading(false);
        }
    };

    const handleHomeClick = async (home) => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`http://localhost:8080/api/homes/${home.id}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                throw new Error('Failed to fetch home details');
            }

            const data = await response.json();
            setSelectedHome(data);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const handleUpdateHome = (updatedHome) => {
        setSelectedHome(updatedHome);
        setHomes(prevHomes => 
            prevHomes.map(home => 
                home.id === updatedHome.id ? updatedHome : home
            )
        );
    };

    const handleUpdateDevice = (roomId, deviceId, updatedDevice) => {
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

    const handleEditHome = (home, e) => {
        e.stopPropagation();
        setHomeToEdit(home);
        setNewHome({
            name: home.name,
            address: home.address,
            timeZone: home.timeZone
        });
        setValidationErrors({});
        setIsEditModalOpen(true);
    };

    const handleDeleteHome = (home, e) => {
        e.stopPropagation();
        setHomeToDelete(home);
        setIsDeleteModalOpen(true);
    };

    const confirmEdit = async () => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const response = await fetch(`http://localhost:8080/api/homes/${homeToEdit.id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify(newHome),
                });

                if (!response.ok) {
                    if (response.status === 400) {
                        const errorData = await response.json();
                        setValidationErrors(errorData);
                        throw new Error('Validation failed');
                    }
                    throw new Error('Failed to update home');
                }

                const updatedHome = await response.json();
                setHomes(prevHomes => 
                    prevHomes.map(home => 
                        home.id === updatedHome.id ? updatedHome : home
                    )
                );
                setIsEditModalOpen(false);
                setHomeToEdit(null);
                setNewHome({ name: '', address: '', timeZone: '' });
                setValidationErrors({});
            } catch (error) {
                if (error.message !== 'Validation failed') {
                    console.error('Error:', error);
                    setValidationErrors({ general: 'Failed to update home. Please try again.' });
                }
            }
        }
    };

    const confirmDelete = async () => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const response = await fetch(`http://localhost:8080/api/homes/${homeToDelete.id}`, {
                    method: 'DELETE',
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                if (!response.ok) {
                    throw new Error('Failed to delete home');
                }

                setHomes(prevHomes => 
                    prevHomes.filter(home => home.id !== homeToDelete.id)
                );
                setIsDeleteModalOpen(false);
                setHomeToDelete(null);
            } catch (error) {
                console.error('Error:', error);
            }
        }
    };

    const handleAddHome = async () => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const response = await fetch(`http://localhost:8080/api/homes/${lastSearchedUser}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify(newHome),
                });

                if (!response.ok) {
                    if (response.status === 400) {
                        const errorData = await response.json();
                        setValidationErrors(errorData);
                        throw new Error('Validation failed');
                    }
                    throw new Error('Failed to add home');
                }

                const data = await response.json();
                setHomes((prevHomes) => [...prevHomes, data]);
                setIsAddModalOpen(false);
                setNewHome({ name: '', address: '', timeZone: '' });
                setValidationErrors({});
            } catch (error) {
                if (error.message !== 'Validation failed') {
                    console.error('Error:', error);
                    setValidationErrors({ general: 'Failed to add home. Please try again.' });
                }
            }
        }
    };

    return (
        <PageWithNavbar>
            <Container>
                <HeaderContainer>
                    <h1>Admin Panel</h1>
                    <SearchSection>
                        <Input
                            type="text"
                            placeholder="Enter username"
                            value={searchUsername}
                            onChange={(e) => setSearchUsername(e.target.value)}
                        />
                        <SearchButton onClick={handleSearch} disabled={loading}>
                            {loading ? 'Searching...' : 'Search'}
                        </SearchButton>
                    </SearchSection>
                    <AddButton 
                        onClick={() => setIsAddModalOpen(true)}
                        disabled={!lastSearchedUser}
                    >
                        Add Home for {lastSearchedUser || 'User'}
                    </AddButton>
                </HeaderContainer>

                {error && <ErrorMessage>{error}</ErrorMessage>}

                {homes.length > 0 ? (
                    <Grid>
                        {homes.map((home) => (
                            <HomeCard key={home.id}>
                                <div onClick={() => handleHomeClick(home)}>
                                    <Icon icon="mdi:home-outline" width="40" height="40" style={{ color: '#4caf50' }} />
                                    <h3>{home.name}</h3>
                                    <p>{home.address}</p>
                                    <p>Time Zone: {home.timeZone}</p>
                                </div>
                                <ActionButtons>
                                    <IconButton onClick={(e) => handleEditHome(home, e)}>
                                        <Icon icon="mdi:pencil" width="20" height="20" />
                                    </IconButton>
                                    <IconButton delete onClick={(e) => handleDeleteHome(home, e)}>
                                        <Icon icon="mdi:trash-can" width="20" height="20" />
                                    </IconButton>
                                </ActionButtons>
                            </HomeCard>
                        ))}
                    </Grid>
                ) : (
                    !loading && !error && <p>No homes found</p>
                )}

                {isEditModalOpen && (
                    <AnimatedModalOverlay>
                        <AnimatedModalContent>
                            <h2>Edit Home</h2>
                            <FormGroup>
                                <Label htmlFor="name">Name</Label>
                                <Input
                                    id="name"
                                    type="text"
                                    value={newHome.name}
                                    onChange={(e) => setNewHome({ ...newHome, name: e.target.value })}
                                />
                                {validationErrors.name && <ErrorMessage>{validationErrors.name}</ErrorMessage>}
                            </FormGroup>
                            <FormGroup>
                                <Label htmlFor="address">Address</Label>
                                <Input
                                    id="address"
                                    type="text"
                                    value={newHome.address}
                                    onChange={(e) => setNewHome({ ...newHome, address: e.target.value })}
                                />
                                {validationErrors.address && <ErrorMessage>{validationErrors.address}</ErrorMessage>}
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
                                {validationErrors.timeZone && <ErrorMessage>{validationErrors.timeZone}</ErrorMessage>}
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
                        </AnimatedModalContent>
                    </AnimatedModalOverlay>
                )}

                {isDeleteModalOpen && (
                    <AnimatedModalOverlay>
                        <AnimatedModalContent>
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
                        </AnimatedModalContent>
                    </AnimatedModalOverlay>
                )}

                {isAddModalOpen && (
                    <AnimatedModalOverlay>
                        <AnimatedModalContent>
                            <h2>Add New Home for {lastSearchedUser}</h2>
                            <FormGroup>
                                <Label htmlFor="name">Name</Label>
                                <Input
                                    id="name"
                                    type="text"
                                    value={newHome.name}
                                    onChange={(e) => setNewHome({ ...newHome, name: e.target.value })}
                                />
                                {validationErrors.name && <ErrorMessage>{validationErrors.name}</ErrorMessage>}
                            </FormGroup>
                            <FormGroup>
                                <Label htmlFor="address">Address</Label>
                                <Input
                                    id="address"
                                    type="text"
                                    value={newHome.address}
                                    onChange={(e) => setNewHome({ ...newHome, address: e.target.value })}
                                />
                                {validationErrors.address && <ErrorMessage>{validationErrors.address}</ErrorMessage>}
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
                                {validationErrors.timeZone && <ErrorMessage>{validationErrors.timeZone}</ErrorMessage>}
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
                                <button onClick={handleAddHome}>Add Home</button>
                            </ModalActions>
                        </AnimatedModalContent>
                    </AnimatedModalOverlay>
                )}

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

export default AdminPanel; 