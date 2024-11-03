import CreateSurvey from '@/components/CreateSurvey';
import { Button } from '@/components/ui/button';
import { FC, useState } from 'react';
import { useTranslation } from 'react-i18next';

const SurveysList: FC = () => {
  const { t } = useTranslation('survey');
  const [createSurveyFormOpen, setCreateSurveyFormOpen] = useState(false);

  return (
    <div>
      Survey list page
      <Button onClick={() => setCreateSurveyFormOpen(!createSurveyFormOpen)}>
        {t('form.createSurvey')}
      </Button>
      <CreateSurvey
        open={createSurveyFormOpen}
        onOpenChange={() => setCreateSurveyFormOpen(!createSurveyFormOpen)}
      />
    </div>
  );
};

export default SurveysList;
