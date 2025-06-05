import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AssociateService from '../api/AssociateService';
import {
    Grid,
    TextField,
    Button,
    Paper,
    Typography,
    Box,
} from '@mui/material';
import { useAssociate } from '../context/AssociateContext';

const AssociateForm = () => {
    const [name, setName] = useState('');
    const [document, setDocument] = useState('');
    
    const navigate = useNavigate();
    
    const { setAssociate } = useAssociate(null);

    const handleSubmit = async () => {
        try {
            const response = await AssociateService.createAssociate({ name, document });
            setAssociate(response.data);
            navigate('/menu');
        } catch (error) {
            alert('Error when trying to register associate' + (error.response?.data?.errorMessage || 'Unknown error'));
        }
    };

    return (
        <Grid
            container
            justifyContent="center"
            alignItems="center"
            sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}
        >
            <Grid item xs={12} sm={9} md={4}>
                <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
                    <Box mb={3} textAlign="center">
                        <Typography variant="h5" component="h2">
                            Register Associate
                        </Typography>
                    </Box>
                    <form>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <TextField
                                    label="Name"
                                    fullWidth
                                    variant="standard"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    required
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    label="Document"
                                    fullWidth
                                    variant="standard"
                                    value={document}
                                    onChange={(e) => setDocument(e.target.value)}
                                    required
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <Button
                                    type="submit"
                                    variant="contained"
                                    color="primary"
                                    fullWidth
                                    onClick={(e) => {
                                        e.preventDefault();
                                        handleSubmit();
                                    }}
                                >
                                    Submit
                                </Button>
                            </Grid>
                        </Grid>
                    </form>
                </Paper>
            </Grid>
        </Grid>
    );
};

export default AssociateForm;