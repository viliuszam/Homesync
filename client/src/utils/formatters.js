export const formatEnumValue = (value) => {
    if (!value) return '';
    
    // Split by underscore and capitalize each word
    return value.split('_')
        .map(word => word.charAt(0) + word.slice(1).toLowerCase())
        .join(' ');
}; 