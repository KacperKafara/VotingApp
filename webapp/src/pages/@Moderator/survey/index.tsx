import { FC } from 'react';
import { useParams } from 'react-router-dom';

const SurveyPage: FC = () => {
  const { id } = useParams<{ id: string }>();
  return <div>{id}</div>;
};

export default SurveyPage;
