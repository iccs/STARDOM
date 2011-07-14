<?php

namespace Iccs\StardomBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\StardomBundle\Entity\Metrics
 */
class Metrics
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $fileId
     */
    private $fileId;

    /**
     * @var integer $commitId
     */
    private $commitId;

    /**
     * @var text $lang
     */
    private $lang;

    /**
     * @var integer $sloc
     */
    private $sloc;

    /**
     * @var integer $loc
     */
    private $loc;

    /**
     * @var integer $ncomment
     */
    private $ncomment;

    /**
     * @var integer $lcomment
     */
    private $lcomment;

    /**
     * @var integer $lblank
     */
    private $lblank;

    /**
     * @var integer $nfunctions
     */
    private $nfunctions;

    /**
     * @var integer $mccabeMax
     */
    private $mccabeMax;

    /**
     * @var integer $mccabeMin
     */
    private $mccabeMin;

    /**
     * @var integer $mccabeSum
     */
    private $mccabeSum;

    /**
     * @var integer $mccabeMean
     */
    private $mccabeMean;

    /**
     * @var integer $mccabeMedian
     */
    private $mccabeMedian;

    /**
     * @var integer $halsteadLength
     */
    private $halsteadLength;

    /**
     * @var integer $halsteadVol
     */
    private $halsteadVol;

    /**
     * @var float $halsteadLevel
     */
    private $halsteadLevel;

    /**
     * @var integer $halsteadMd
     */
    private $halsteadMd;


    /**
     * Get id
     *
     * @return integer 
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set fileId
     *
     * @param integer $fileId
     */
    public function setFileId($fileId)
    {
        $this->fileId = $fileId;
    }

    /**
     * Get fileId
     *
     * @return integer 
     */
    public function getFileId()
    {
        return $this->fileId;
    }

    /**
     * Set commitId
     *
     * @param integer $commitId
     */
    public function setCommitId($commitId)
    {
        $this->commitId = $commitId;
    }

    /**
     * Get commitId
     *
     * @return integer 
     */
    public function getCommitId()
    {
        return $this->commitId;
    }

    /**
     * Set lang
     *
     * @param text $lang
     */
    public function setLang($lang)
    {
        $this->lang = $lang;
    }

    /**
     * Get lang
     *
     * @return text 
     */
    public function getLang()
    {
        return $this->lang;
    }

    /**
     * Set sloc
     *
     * @param integer $sloc
     */
    public function setSloc($sloc)
    {
        $this->sloc = $sloc;
    }

    /**
     * Get sloc
     *
     * @return integer 
     */
    public function getSloc()
    {
        return $this->sloc;
    }

    /**
     * Set loc
     *
     * @param integer $loc
     */
    public function setLoc($loc)
    {
        $this->loc = $loc;
    }

    /**
     * Get loc
     *
     * @return integer 
     */
    public function getLoc()
    {
        return $this->loc;
    }

    /**
     * Set ncomment
     *
     * @param integer $ncomment
     */
    public function setNcomment($ncomment)
    {
        $this->ncomment = $ncomment;
    }

    /**
     * Get ncomment
     *
     * @return integer 
     */
    public function getNcomment()
    {
        return $this->ncomment;
    }

    /**
     * Set lcomment
     *
     * @param integer $lcomment
     */
    public function setLcomment($lcomment)
    {
        $this->lcomment = $lcomment;
    }

    /**
     * Get lcomment
     *
     * @return integer 
     */
    public function getLcomment()
    {
        return $this->lcomment;
    }

    /**
     * Set lblank
     *
     * @param integer $lblank
     */
    public function setLblank($lblank)
    {
        $this->lblank = $lblank;
    }

    /**
     * Get lblank
     *
     * @return integer 
     */
    public function getLblank()
    {
        return $this->lblank;
    }

    /**
     * Set nfunctions
     *
     * @param integer $nfunctions
     */
    public function setNfunctions($nfunctions)
    {
        $this->nfunctions = $nfunctions;
    }

    /**
     * Get nfunctions
     *
     * @return integer 
     */
    public function getNfunctions()
    {
        return $this->nfunctions;
    }

    /**
     * Set mccabeMax
     *
     * @param integer $mccabeMax
     */
    public function setMccabeMax($mccabeMax)
    {
        $this->mccabeMax = $mccabeMax;
    }

    /**
     * Get mccabeMax
     *
     * @return integer 
     */
    public function getMccabeMax()
    {
        return $this->mccabeMax;
    }

    /**
     * Set mccabeMin
     *
     * @param integer $mccabeMin
     */
    public function setMccabeMin($mccabeMin)
    {
        $this->mccabeMin = $mccabeMin;
    }

    /**
     * Get mccabeMin
     *
     * @return integer 
     */
    public function getMccabeMin()
    {
        return $this->mccabeMin;
    }

    /**
     * Set mccabeSum
     *
     * @param integer $mccabeSum
     */
    public function setMccabeSum($mccabeSum)
    {
        $this->mccabeSum = $mccabeSum;
    }

    /**
     * Get mccabeSum
     *
     * @return integer 
     */
    public function getMccabeSum()
    {
        return $this->mccabeSum;
    }

    /**
     * Set mccabeMean
     *
     * @param integer $mccabeMean
     */
    public function setMccabeMean($mccabeMean)
    {
        $this->mccabeMean = $mccabeMean;
    }

    /**
     * Get mccabeMean
     *
     * @return integer 
     */
    public function getMccabeMean()
    {
        return $this->mccabeMean;
    }

    /**
     * Set mccabeMedian
     *
     * @param integer $mccabeMedian
     */
    public function setMccabeMedian($mccabeMedian)
    {
        $this->mccabeMedian = $mccabeMedian;
    }

    /**
     * Get mccabeMedian
     *
     * @return integer 
     */
    public function getMccabeMedian()
    {
        return $this->mccabeMedian;
    }

    /**
     * Set halsteadLength
     *
     * @param integer $halsteadLength
     */
    public function setHalsteadLength($halsteadLength)
    {
        $this->halsteadLength = $halsteadLength;
    }

    /**
     * Get halsteadLength
     *
     * @return integer 
     */
    public function getHalsteadLength()
    {
        return $this->halsteadLength;
    }

    /**
     * Set halsteadVol
     *
     * @param integer $halsteadVol
     */
    public function setHalsteadVol($halsteadVol)
    {
        $this->halsteadVol = $halsteadVol;
    }

    /**
     * Get halsteadVol
     *
     * @return integer 
     */
    public function getHalsteadVol()
    {
        return $this->halsteadVol;
    }

    /**
     * Set halsteadLevel
     *
     * @param float $halsteadLevel
     */
    public function setHalsteadLevel($halsteadLevel)
    {
        $this->halsteadLevel = $halsteadLevel;
    }

    /**
     * Get halsteadLevel
     *
     * @return float 
     */
    public function getHalsteadLevel()
    {
        return $this->halsteadLevel;
    }

    /**
     * Set halsteadMd
     *
     * @param integer $halsteadMd
     */
    public function setHalsteadMd($halsteadMd)
    {
        $this->halsteadMd = $halsteadMd;
    }

    /**
     * Get halsteadMd
     *
     * @return integer 
     */
    public function getHalsteadMd()
    {
        return $this->halsteadMd;
    }
}