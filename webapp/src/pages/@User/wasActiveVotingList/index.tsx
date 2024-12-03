import VotingListComponent from '@/components/voting/VotingList';
import { FC } from 'react';

const VotingListEverActive: FC = () => {
  return <VotingListComponent type="user" wasActive={true} />;
};

export default VotingListEverActive;
