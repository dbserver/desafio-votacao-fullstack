import RestConsumer from './RestConsumer';


const AssociateService = {
  createAssociate: (associate) =>
    RestConsumer.post(
      '/associate',
      associate,
      {
        'Content-Type': 'application/json'
      }
    ),
  getAssociateByDocument: (document) =>
    RestConsumer.get(`getAssociateByDocument?document=${document}`),
};

export default AssociateService;
