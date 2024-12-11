import React, { useState } from 'react';
import styled from 'styled-components';
import { formatEnumValue } from '../utils/formatters';

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

const ErrorMessage = styled.div`
    color: #dc3545;
    font-size: 0.875rem;
    margin-top: 0.25rem;
`;

const ModalActions = styled.div`
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 1rem;
`;

const ServerError = styled.div`
    background-color: #fff3f3;
    color: #dc3545;
    padding: 1rem;
    border-radius: 4px;
    margin-bottom: 1rem;
    border: 1px solid #dc3545;
`;

const DEVICE_TYPES = [
    'LIGHT',
    'THERMOSTAT',
    'SPEAKER',
    'SECURITY_CAMERA',
    'DOOR_LOCK',
    'SMOKE_DETECTOR',
    'VACUUM_CLEANER',
    'REFRIGERATOR',
    'OVEN',
    'DISHWASHER',
    'WASHING_MACHINE'
];

const DeviceFormModal = ({ device, onClose, onSubmit, title }) => {
    const [formData, setFormData] = useState(device || {
        name: '',
        deviceType: '',
        manufacturer: '',
        state: false,
        powerConsumption: 0
    });
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState('');

    const handleSubmit = async () => {
        try {
            setServerError('');
            setErrors({});
            await onSubmit(formData);
            onClose();
        } catch (error) {
            if (error.validationErrors) {
                setErrors(error.validationErrors);
            } else {
                setServerError('An error occurred while saving the device. Please try again.');
            }
        }
    };

    return (
        <ModalContent onClick={e => e.stopPropagation()}>
            <h2>{title}</h2>
            {serverError && <ServerError>{serverError}</ServerError>}
            <FormGroup>
                <Label htmlFor="name">Device Name</Label>
                <Input
                    id="name"
                    value={formData.name}
                    onChange={e => setFormData({ ...formData, name: e.target.value })}
                />
                {errors.name && <ErrorMessage>{errors.name}</ErrorMessage>}
            </FormGroup>
            <FormGroup>
                <Label htmlFor="deviceType">Device Type</Label>
                <Select
                    id="deviceType"
                    value={formData.deviceType}
                    onChange={e => setFormData({ ...formData, deviceType: e.target.value })}
                >
                    <option value="">Select a device type</option>
                    {DEVICE_TYPES.map(type => (
                        <option key={type} value={type}>
                            {formatEnumValue(type)}
                        </option>
                    ))}
                </Select>
                {errors.deviceType && <ErrorMessage>{errors.deviceType}</ErrorMessage>}
            </FormGroup>
            <FormGroup>
                <Label htmlFor="manufacturer">Manufacturer</Label>
                <Input
                    id="manufacturer"
                    value={formData.manufacturer}
                    onChange={e => setFormData({ ...formData, manufacturer: e.target.value })}
                />
                {errors.manufacturer && <ErrorMessage>{errors.manufacturer}</ErrorMessage>}
            </FormGroup>
            <FormGroup>
                <Label htmlFor="powerConsumption">Power Consumption (watts)</Label>
                <Input
                    id="powerConsumption"
                    type="number"
                    value={formData.powerConsumption}
                    onChange={e => setFormData({ ...formData, powerConsumption: Number(e.target.value) })}
                />
                {errors.powerConsumption && <ErrorMessage>{errors.powerConsumption}</ErrorMessage>}
            </FormGroup>
            <ModalActions>
                <button onClick={onClose}>Cancel</button>
                <button onClick={handleSubmit}>
                    {device ? 'Save Changes' : 'Create Device'}
                </button>
            </ModalActions>
        </ModalContent>
    );
};

export default DeviceFormModal; 