import CreateSurvey from '@/components/CreateSurvey';
import SurveyList from '@/components/survey/SurveysList';
import { Button } from '@/components/ui/button';
import { FC, useState } from 'react';
import { useTranslation } from 'react-i18next';

const SurveysList: FC = () => {
  const { t } = useTranslation('survey');
  const [createSurveyFormOpen, setCreateSurveyFormOpen] = useState(false);

  return (
    <>
      <SurveyList>
        <Button onClick={() => setCreateSurveyFormOpen(!createSurveyFormOpen)}>
          {t('form.createSurvey')}
        </Button>
      </SurveyList>
      <CreateSurvey
        open={createSurveyFormOpen}
        onOpenChange={() => setCreateSurveyFormOpen(!createSurveyFormOpen)}
      />
    </>
  );
};

export default SurveysList;
